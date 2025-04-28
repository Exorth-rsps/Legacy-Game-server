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
import kotlin.streams.toList

/**
 * HighScoresController leest highscores uit alle speler-savebestanden (offline én online).
 * Routes: /highscores en /highscores/skill/:skillId
 */
class HighScoresController(
    private val req: Request,
    private val resp: Response,
    auth: Boolean
) : Controller(req, resp, auth) {

    private val logger = KotlinLogging.logger {}
    private val gson = Gson()
    private val skillsCount = 23

    /**
     * Probeer meerdere paden om de saves map te vinden.
     */
    private fun resolveSavesDir(): Path? {
        val candidates = listOf(
            Paths.get("data", "saves"),
            Paths.get("..", "data", "saves"),
            Paths.get("..", "..", "data", "saves")
        )
        return candidates.firstOrNull { Files.isDirectory(it) }
    }

    override fun init(world: World): JsonObject {
        // 1) Skill-id uit path of query (?skill=…)
        val skillId = req.params("skillId")?.toIntOrNull()
            ?: req.queryParams("skill")?.toIntOrNull()
            ?: -1

        // 2) Limiet (default 10)
        val limit = req.queryParams("limit")?.toIntOrNull() ?: 10

        // 3) Vind saves-directory
        val savesDir = resolveSavesDir()
        if (savesDir == null) {
            logger.warn("Geen data/saves-map gevonden op bekende paden, alleen online spelers worden meegenomen.")
        } else {
            logger.info("Using saves directory: $savesDir")
        }
        val filesStream = savesDir?.let { Files.list(it) } ?: java.util.stream.Stream.empty<Path>()

        // 4) Lees alle player-save JSON-bestanden met logging
        data class PlayerSave(val username: String, val xp: List<Double>)
        val allPlayers = filesStream
            .filter { path ->
                val isJson = path.fileName.toString().endsWith(".json")
                if (!isJson) logger.debug("Skipping non-JSON file: $path")
                isJson
            }
            .map { path ->
                logger.info("Loading save file: $path")
                val jo = gson.fromJson(Files.newBufferedReader(path), JsonObject::class.java)
                val username = jo.get("username").asString
                val skillsJson = jo.getAsJsonArray("skills")
                val xpList = (0 until skillsJson.size()).map { idx ->
                    skillsJson[idx].asJsonObject.get("xp").asDouble
                }
                PlayerSave(username, xpList)
            }
            .toList()
        logger.info("Total save files loaded: ${allPlayers.size}")

        // 5) Sorteer op skill of totaal XP
        val sorted = if (skillId in 0 until skillsCount) {
            allPlayers.sortedByDescending { it.xp[skillId] }
        } else {
            allPlayers.sortedByDescending { it.xp.sum() }
        }

        // 6) Bouw JSON-response
        return JsonObject().apply {
            if (skillId in 0 until skillsCount) {
                addProperty("skill", skillId)
            } else {
                addProperty("skill", "overall")
            }
            addProperty("count", allPlayers.size)
            addProperty("limit", limit)

            val hsArr = JsonArray().apply {
                sorted.take(limit).forEachIndexed { idx, ps ->
                    add(JsonObject().apply {
                        addProperty("rank", idx + 1)
                        addProperty("username", ps.username)
                        if (skillId in 0 until skillsCount) {
                            addProperty("xp", ps.xp[skillId])
                        } else {
                            addProperty("totalXp", ps.xp.sum())
                        }
                    })
                }
            }
            add("highscores", hsArr)
        }
    }
}
