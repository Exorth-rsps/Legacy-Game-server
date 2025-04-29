package org.alter.plugins.service.restapi.controllers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.Gson
import io.github.oshai.kotlinlogging.KotlinLogging
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

        // Vind directory
        val savesDir = resolveSavesDir()
            ?: throw RuntimeException("Saves directory niet gevonden")
        val saveFile = savesDir.resolve("${'$'}username.json")

        if (Files.exists(saveFile)) {
            try {
                val jo = gson.fromJson(Files.newBufferedReader(saveFile), JsonObject::class.java)
                // Bouw JSON-response
                val pObj = JsonObject().apply {
                    addProperty("username", jo.get("username").asString)
                    addProperty("privilege", jo.get("privilege").asInt)
                    addProperty("gameMode", jo.get("displayMode").asInt)
                    addProperty("combatLvl", jo.get("combatLvl")?.asInt
                        ?: jo.get("combatLevel").asInt)
                    addProperty("isOnline", false)
                    addProperty("xpRate", jo.get("xpRate")?.asInt ?: 0)
                    addProperty("UID", jo.get("username").asString)

                    // Skills
                    val skillArr = JsonArray()
                    jo.getAsJsonArray("skills").forEach { elem ->
                        val sk = elem.asJsonObject
                        val skillObj = JsonObject().apply {
                            addProperty("skillId", sk.get("skill").asInt)
                            addProperty("xp", sk.get("xp").asDouble)
                            addProperty("lvl", sk.get("lvl").asInt)
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

    /**
     * Zoek de 'data/saves' directory via relative paden
     */
    private fun resolveSavesDir(): Path? {
        val candidates = listOf(
            Paths.get("data", "saves"),
            Paths.get("..", "data", "saves"),
            Paths.get("..", "..", "data", "saves")
        )
        return candidates.firstOrNull { Files.isDirectory(it) }
    }
}
