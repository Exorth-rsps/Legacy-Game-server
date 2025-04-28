package org.alter.plugins.service.restapi.controllers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.Gson
import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import spark.Request
import spark.Response
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

/**
 * HighScoresController haalt de highscores uit alle player-save bestanden (offline én online).
 * Endpoints blijven hetzelfde: /highscores en /highscores/skill/:skillId
 */
class HighScoresController(
    private val req: Request,
    private val resp: Response,
    auth: Boolean
) : Controller(req, resp, auth) {

    private val logger = KotlinLogging.logger {}
    private val gson = Gson()
    // Bouw een pad vanaf de werkdirectory: ${'$'}{user.dir}/data/saves
    // Relative path vanuit werkdirectory (project root tijdens run): data/saves
    private val savesDir: Path = Paths.get("data", "saves")
    private val skillsCount = 23 = 23

    override fun init(world: World): JsonObject {
        // Controleer of savesDir bestaat; anders fallback naar alleen online spelers
        val saveDirStream = if (Files.exists(savesDir) && Files.isDirectory(savesDir)) {
            Files.list(savesDir)
        } else {
            logger.warn("Save directory $savesDir bestaat niet of is geen map, gebruik alleen online spelers als fallback.")
            java.util.stream.Stream.empty<Path>()
        }

        // 1) Skill-id uit path of query (?skill=…)
        val skillId = req.params("skillId")?.toIntOrNull()
            ?: req.queryParams("skill")?.toIntOrNull()
            ?: -1

        // 2) Hoeveel topscores (default 10)
        val limit = req.queryParams("limit")?.toIntOrNull() ?: 10

        // 3) Lees alle player-save JSON-bestanden
        data class PlayerSave(val username: String, val xp: List<Double>)
        val allPlayers = saveDirStream
            .filter { it.fileName.toString().endsWith(".json") }
            .map { path ->
                val jo = gson.fromJson(Files.newBufferedReader(path), JsonObject::class.java)
                val username = jo.get("username").asString
                val skillsJson = jo.getAsJsonArray("skills")
                val xpList = (0 until skillsJson.size()).map { idx ->
                    skillsJson[idx].asJsonObject.get("xp").asDouble
                }
                PlayerSave(username, xpList)
            }
            .toList()

        // 4) Sorteer op skill of overall
        val sorted = if (skillId in 0 until skillsCount) {
            allPlayers.sortedByDescending { it.xp[skillId] }
        } else {
            allPlayers.sortedByDescending { it.xp.sum() }
        }

        // 5) Bouw response JSON
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
