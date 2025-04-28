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
    private val skillsCount = 25  // Aantal skills in jouw JSON-structuur

    /**
     * Probeer meerdere paden om de saves map te vinden, met debug-logging per kandidaat.
     */
    private fun resolveSavesDir(): Path? {
        val candidates = listOf(
            Paths.get("data", "saves"),
            Paths.get("..", "data", "saves"),
            Paths.get("..", "..", "data", "saves")
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
        val savesDir = resolveSavesDir().also {
            if (it == null) logger.warn("Geen data/saves-map gevonden, alleen online spelers.")
            else logger.info("Using saves directory: $it")
        }
        val stream = savesDir?.let { Files.list(it) } ?: java.util.stream.Stream.empty<Path>()

        // 4) Lees en parse bestanden met foutafhandeling
        data class PlayerSave(val username: String, val xp: List<Double>, val lvl: List<Int>)
        val successes = mutableListOf<PlayerSave>()
        val failed = mutableListOf<String>()

        stream.filter { Files.isRegularFile(it) }
            .forEach { path ->
                try {
                    logger.info("Loading save file: $path")
                    val jo = gson.fromJson(Files.newBufferedReader(path), JsonObject::class.java)
                    val username = jo.get("username").asString
                    val skillsJson = jo.getAsJsonArray("skills")
                    val xpList = mutableListOf<Double>()
                    val lvlList = mutableListOf<Int>()
                    for (i in 0 until skillsJson.size()) {
                        val sk = skillsJson[i].asJsonObject
                        xpList.add(sk.get("xp").asDouble)
                        lvlList.add(sk.get("lvl").asInt)
                    }
                    successes.add(PlayerSave(username, xpList, lvlList))
                } catch (e: Exception) {
                    logger.error(e) { "Failed to load save file: $path" }
                    failed.add(path.fileName.toString())
                }
            }
        logger.info("Save files loaded: ${'$'}{successes.size}, failed: ${'$'}{failed.size}")

        // 5) Sorteer op skill of totaal XP
        val sorted = if (skillId in 0 until skillsCount) {
            successes.sortedByDescending { it.xp[skillId] }
        } else {
            successes.sortedByDescending { it.xp.sum() }
        }

        // 6) Bouw JSON-response
        val obj = JsonObject()
        // skill-metadata
        if (skillId in 0 until skillsCount) {
            obj.addProperty("skill", skillId)
        } else {
            obj.addProperty("skill", "overall")
        }
        obj.addProperty("countLoaded", successes.size)
        obj.addProperty("countFailed", failed.size)
        obj.add("failedFiles", JsonArray().apply { failed.forEach { add(it) } })

        // highscores
        val hsArr = JsonArray().apply {
            sorted.take(limit).forEachIndexed { idx, ps ->
                add(JsonObject().apply {
                    addProperty("rank", idx + 1)
                    addProperty("username", ps.username)
                    if (skillId in 0 until skillsCount) {
                        addProperty("xp", ps.xp[skillId])
                        addProperty("lvl", ps.lvl[skillId])
                    } else {
                        addProperty("totalXp", ps.xp.sum())
                        // Voeg total level toe voor overall
                        val totalLvl = ps.lvl.sum()
                        addProperty("lvl", totalLvl)
                    }
                })
            }
        }
        obj.add("highscores", hsArr)
        return obj
    }
}
