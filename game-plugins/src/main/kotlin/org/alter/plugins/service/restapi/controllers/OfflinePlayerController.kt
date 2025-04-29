package org.alter.plugins.service.restapi.controllers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.Gson
import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.api.Skills
import org.alter.game.model.World
import spark.Request
import spark.Response
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * OfflinePlayerController haalt spelersgegevens uit save-bestanden.
 * Route: GET /offline-player/:name
 */
class OfflinePlayerController(
    private val req: Request,
    private val resp: Response,
    auth: Boolean
) : Controller(req, resp, auth) {

    private val logger = KotlinLogging.logger {}
    private val gson = Gson()

    override fun init(world: World): JsonObject {
        val obj = JsonObject()
        val userArr = JsonArray()
        val username = req.params("name") ?: return obj

        // Zoek data/saves directory
        val savesDir = resolveSavesDir()
            ?: throw RuntimeException("Saves directory niet gevonden")

        // Ondersteun bestandsnaam met en zonder .json
        val fileJson  = savesDir.resolve("$username.json")
        val filePlain = savesDir.resolve(username)
        val saveFile = when {
            Files.exists(fileJson)  -> fileJson
            Files.exists(filePlain) -> filePlain
            else -> null
        }

        if (saveFile != null) {
            try {
                val jo = gson.fromJson(Files.newBufferedReader(saveFile), JsonObject::class.java)
                val pObj = JsonObject().apply {
                    addProperty("username", jo.get("username").asString)
                    addProperty("privilege", jo.get("privilege").asInt)
                    addProperty("gameMode", jo.get("displayMode")?.asInt ?: 0)

                    // Bepaal combat level veilig
                    val combatLvl = when {
                        jo.has("combatLvl")    -> jo.get("combatLvl").asInt
                        jo.has("combatLevel")  -> jo.get("combatLevel").asInt
                        else                     -> 0
                    }
                    addProperty("combatLvl", combatLvl)

                    addProperty("isOnline", false)
                    addProperty("xpRate", jo.get("xpRate")?.asInt ?: 0)
                    addProperty("UID", jo.get("username").asString)

                    // Skills
                    val skillArr = JsonArray()
                    jo.getAsJsonArray("skills").forEach { elem ->
                        val sk = elem.asJsonObject
                        val skillId = sk.get("skill").asInt
                        val skillName = Skills.getSkillName(world, skillId)
                        val skillObj = JsonObject().apply {
                            addProperty("name", skillName)
                            addProperty("currentLevel", sk.get("lvl").asInt)
                        }
                        skillArr.add(skillObj)
                    }
                    add("skills", skillArr)
                }
                userArr.add(pObj)
            } catch (e: Exception) {
                logger.error(e) { "Kon save-bestand niet lezen: $saveFile" }
            }
        } else {
            logger.warn("Offline save-file niet gevonden voor speler: $username")
        }

        obj.add("player", userArr)
        return obj
    }

    /** Vind de data/saves directory via relative paden */
    private fun resolveSavesDir(): Path? {
        val candidates = listOf(
            Paths.get("data", "saves"),
            Paths.get("..", "data", "saves"),
            Paths.get("..", "..", "data", "saves")
        )
        candidates.forEach { path ->
            logger.info("Checking savesDir: $path (exists=${Files.exists(path)}, dir=${Files.isDirectory(path)})")
        }
        return candidates.firstOrNull { Files.isDirectory(it) }
    }
}
