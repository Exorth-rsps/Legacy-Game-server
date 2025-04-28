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
    private val skillsCount = 25  // Aantal skills in je JSON

    /**
     * Probeer meerdere paden om de saves map te vinden, met debug-logging per kandidaat.
     */
    private fun resolveSavesDir(): Path? {
        val candidates = listOf(
            Paths.get("data", "saves"),          // project root
            Paths.get("..", "data", "saves"),   // één niveau omhoog
            Paths.get("..", "..", "data", "saves") // twee niveaus omhoog
        )
        candidates.forEach { path ->
            logger.info("Checking savesDir candidate: $path (exists=${Files.exists(path)}, isDir=${Files.isDirectory(path)})")
        }
        return candidates.firstOrNull { Files.isDirectory(it) }
    }

    override fun init(world: World): JsonObject {
        // 1) Skill-id uit path of query (?skill=…)
        val skillId = req.params("skillId")?.toIntOrNull()
            ?: req.queryParams("skill")?.toIntOrNull()
            ?: -1

        // 2) Limiet (default 10)
        val limit = req.queryParams("limit")?.toIntOrNull() ?: 10

        // 3) Vind saves-directory en lees bestanden
        val savesDir = resolveSavesDir()
        if (savesDir == null) {
            logger.warn("Geen data/saves-map gevonden op bekende paden, alleen online spelers worden meegenomen.")
        } else {
            logger.info("Using saves directory: $savesDir")
        }
        val stream = savesDir?.let { Files.list(it) } ?: java.util.stream.Stream.empty<Path>()

        // 4) Lees alle player-save JSON-bestanden
        data class PlayerSave(val username: String, val xp: List<Double>)

        val allPlayers = stream
            .filter { path ->
                Files.isRegularFile(path)
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

        logger.info("Total save files loaded: ${'$'}{allPlayers.size}")

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
                            // lvl niet beschikbaar in save, gebruik xp-only of lees lvl uit JSON
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
