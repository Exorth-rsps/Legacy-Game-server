package org.alter.game.service.serializer.json

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.alter.game.Server
import org.alter.game.model.PlayerUID
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.appearance.Appearance
import org.alter.game.model.appearance.Gender
import org.alter.game.model.attr.AttributeKey
import org.alter.game.model.container.ItemContainer
import org.alter.game.model.entity.Client
import org.alter.game.model.interf.DisplayMode
import org.alter.game.model.item.Item
import org.alter.game.model.priv.Privilege
import org.alter.game.model.social.Social
import org.alter.game.model.timer.TimerKey
import org.alter.game.service.serializer.PlayerLoadResult
import org.alter.game.service.serializer.PlayerSerializerService
import gg.rsmod.net.codec.login.LoginRequest
import gg.rsmod.util.ServerProperties

import io.github.oshai.kotlinlogging.KotlinLogging
import org.mindrot.jbcrypt.BCrypt
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * Een [PlayerSerializerService] implementatie die spelersdata in JSON decodeert en encodeert,
 * waarbij de bestandsnaam voor elke speler altijd met een hoofdletter begint.
 */
class JsonPlayerSerializer : PlayerSerializerService() {

    private lateinit var path: Path

    override fun initSerializer(server: org.alter.game.Server, world: World, serviceProperties: ServerProperties) {
        path = Paths.get(serviceProperties.getOrDefault("path", "../data/saves/"))
        if (!Files.exists(path)) {
            Files.createDirectory(path)
            logger.info("Path does not exist: $path, creating directory...")
        }
    }

    /**
     * Haal de “gekapitaliseerde” bestandsnaam op basis van loginUsername.
     * Bijvoorbeeld: als loginUsername = "janKlaassen", dan wordt fileName = "JanKlaassen".
     */
    private fun getCapitalizedFileName(loginUsername: String): String {
        return loginUsername.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    override fun loadClientData(client: Client, request: LoginRequest): PlayerLoadResult {
        // 1) Bepaal de bestandsnaam met hoofdletter
        val fileName = getCapitalizedFileName(client.loginUsername)
        val save = path.resolve(fileName)

        // 2) Als er nog geen bestand is met die naam, maak dan een nieuw account aan
        if (!Files.exists(save)) {
            configureNewPlayer(client, request)
            client.uid = PlayerUID(client.loginUsername)
            // Sla het nieuwe account direct op, onder de ge- kapitaliseerde bestandsnaam
            saveClientData(client)
            return PlayerLoadResult.NEW_ACCOUNT
        }

        // 3) Probeer het bestaande bestand in te lezen
        try {
            val world = client.world
            val reader = Files.newBufferedReader(save)
            val json = Gson()
            val data = json.fromJson(reader, JsonPlayerSaveData::class.java)
            reader.close()

            // 3a) Wachtwoordverificatie bij normale login (niet-reconnect)
            if (!request.reconnecting) {
                if (!BCrypt.checkpw(request.password, data.passwordHash)) {
                    return PlayerLoadResult.INVALID_CREDENTIALS
                }
            } else {
                // 3b) Bij reconnect-check de XTEA-keys
                if (!Arrays.equals(data.previousXteas, request.xteaKeys)) {
                    return PlayerLoadResult.INVALID_RECONNECTION
                }
            }

            // 4) Vul het client-object met de ingelezen gegevens
            client.loginUsername = data.username
            client.uid = PlayerUID(data.username)
            client.username = data.displayName
            client.passwordHash = data.passwordHash
            client.tile = Tile(data.x, data.z, data.height)
            client.privilege = world.privileges.get(data.privilege) ?: Privilege.DEFAULT
            client.runEnergy = data.runEnergy
            client.interfaces.displayMode = DisplayMode.values.firstOrNull { it.id == data.displayMode }
                ?: DisplayMode.FIXED
            client.appearance = Appearance(
                data.appearance.looks,
                data.appearance.colors,
                Gender.values.firstOrNull { it.id == data.appearance.gender } ?: Gender.MALE
            )
            data.skills.forEach { skill ->
                client.getSkills().setXp(skill.skill, skill.xp)
                client.getSkills().setCurrentLevel(skill.skill, skill.lvl)
            }
            data.itemContainers.forEach {
                val key = world.plugins.containerKeys.firstOrNull { other -> other.name == it.name }
                if (key == null) {
                    logger.error { "Container was found in serialized data, maar is niet geregistreerd in de World. [key=${it.name}]" }
                    return@forEach
                }
                val container = if (client.containers.containsKey(key)) client.containers[key]
                else {
                    client.containers[key] = ItemContainer(client.world.definitions, key)
                    client.containers[key]
                }!!
                it.items.forEach { slot, item ->
                    container[slot] = item
                }
            }
            data.attributes.forEach { (key, value) ->
                val attribute = AttributeKey<Any>(key)
                client.attr[attribute] = if (value is Double) value.toInt() else value
            }
            data.timers.forEach { timer ->
                var time = timer.timeLeft
                if (timer.tickOffline) {
                    val elapsed = System.currentTimeMillis() - timer.currentMs
                    val ticks = (elapsed / client.world.gameContext.cycleTime).toInt()
                    time -= ticks
                }
                val key = TimerKey(timer.identifier, timer.tickOffline)
                client.timers[key] = Math.max(0, time)
            }
            data.varps.forEach { varp ->
                client.varps.setState(varp.id, varp.state)
            }

            if (data.social == null) data.social = Social()
            client.social = data.social

            return PlayerLoadResult.LOAD_ACCOUNT
        } catch (e: Exception) {
            logger.error(e) { "Error when loading player: ${request.username}" }
            return PlayerLoadResult.MALFORMED
        }
    }

    override fun saveClientData(client: Client): Boolean {
        // 1) Bepaal de “gekapitaliseerde” bestandsnaam
        val fileName = getCapitalizedFileName(client.loginUsername)

        // 2) Bouw de JsonPlayerSaveData op met álle benodigde velden
        val data = JsonPlayerSaveData(
            passwordHash   = client.passwordHash,
            username       = client.loginUsername,
            previousXteas  = client.currentXteaKeys,
            displayName    = client.username,
            x              = client.tile.x,
            z              = client.tile.z,
            height         = client.tile.height,
            privilege      = client.privilege.id,
            runEnergy      = client.runEnergy,
            displayMode    = client.interfaces.displayMode.id,
            appearance     = client.getPersistentAppearance(),
            skills         = client.getPersistentSkills(),
            itemContainers = client.getPersistentContainers(),
            attributes     = client.attr.toPersistentMap(),
            timers         = client.timers.toPersistentTimers(),
            varps          = client.varps.getAll().filter { it.state != 0 },
            social         = client.social
        )

        // 3) Schrijf de JSON naar het “gekapitaliseerde” bestand
        val writer = Files.newBufferedWriter(path.resolve(fileName))
        val json   = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
        json.toJson(data, writer)
        writer.close()
        return true
    }

    /**
     * Helper om in de Client een lijst van niet-lege containers in “PersistentContainer”-vorm
     * te krijgen. Dit verandert niet.
     */
    private fun Client.getPersistentContainers(): List<PersistentContainer> {
        val persistent = mutableListOf<PersistentContainer>()
        containers.forEach { (key, container) ->
            if (!container.isEmpty) {
                persistent.add(PersistentContainer(key.name, container.toMap()))
            }
        }
        return persistent
    }

    /**
     * Helper om in de Client alle skill-gegevens in “PersistentSkill”-vorm terug te krijgen.
     * Dit verandert niet.
     */
    private fun Client.getPersistentSkills(): List<PersistentSkill> {
        val skills = mutableListOf<PersistentSkill>()
        for (i in 0 until getSkills().maxSkills) {
            val xp = getSkills().getCurrentXp(i)
            val lvl = getSkills().getCurrentLevel(i)
            skills.add(PersistentSkill(skill = i, xp = xp, lvl = lvl))
        }
        return skills
    }

    /**
     * Helper om de appearance-gegevens te halen in “PersistentAppearance”-vorm.
     * Dit verandert niet.
     */
    private fun Client.getPersistentAppearance(): PersistentAppearance =
        PersistentAppearance(appearance.gender.id, appearance.looks, appearance.colors)

    data class PersistentAppearance(
        @JsonProperty("gender") val gender: Int,
        @JsonProperty("looks") val looks: IntArray,
        @JsonProperty("colors") val colors: IntArray
    )

    data class PersistentContainer(
        @JsonProperty("name") val name: String,
        @JsonProperty("items") val items: Map<Int, Item>
    )

    data class PersistentSkill(
        @JsonProperty("skill") val skill: Int,
        @JsonProperty("xp") val xp: Double,
        @JsonProperty("lvl") val lvl: Int
    )

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}
