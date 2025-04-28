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
import kotlin.math.floor
import kotlin.math.max
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
            logger.info("Checking savesDir: $path (exists=${Files.exists(path)}, dir=${Files.isDirectory(path)})")
        }
        return candidates.firstOrNull { Files.isDirectory(it) }
    }

    /**
     * Bereken combat level op basis van skill levels volgens OSRS-formule.
     * Combat level = floor(
     *   0.25 * (def + hp + floor(prayer / 2)) +
     *   0.325 * max(
     *     attack + strength,
     *     floor(range * 1.5),
     *     floor(magic * 1.5)
     *   )
     *) clamped at max 126
     */
    private fun calcCombatLevel(lvl: List<Int>): Int {
        val att  = lvl.getOrNull(0) ?: 1
        val def  = lvl.getOrNull(1) ?: 1
        val str  = lvl.getOrNull(2) ?: 1
        val hp   = lvl.getOrNull(3) ?: 10
        val rng  = lvl.getOrNull(4) ?: 1
        val pray = lvl.getOrNull(5) ?: 1
        val mag  = lvl.getOrNull(6) ?: 1

        val base   = 0.25 * (def + hp + floor(pray / 2.0))
        val melee  = 0.325 * (att + str)
        val range  = 0.325 * floor(rng * 1.5)
        val magic  = 0.325 * floor(mag * 1.5)
        val combat = floor(base + max(melee, max(range, magic))).toInt()
        return combat.coerceAtMost(126)
    }

    override fun init(world: World): JsonObject {
        val skillId = req.params("skillId")?.toIntOrNull() ?: req.queryParams("skill")?.toIntOrNull() ?: -1
        val limit   = req.queryParams("limit")?.toIntOrNull() ?: 10

        val savesDir = resolveSavesDir().also {
            if (it == null) logger.warn("Geen saves-dir gevonden, fallback naar enkel online spelers.")
            else logger.info("Using savesDir: $it")
        }
        val files = savesDir?.let { Files.list(it) } ?: java.util.stream.Stream.empty<Path>()

        data class PlayerSave(
            val username: String,
            val xp: List<Double>,
            val lvl: List<Int>,
            val privilege: Int,
            val combatLevel: Int
        )
        val loaded = mutableListOf<PlayerSave>()
        val failed = mutableListOf<String>()

        files.filter { Files.isRegularFile(it) }.forEach { path ->
            try {
                val jo = gson.fromJson(Files.newBufferedReader(path), JsonObject::class.java)
                val username  = jo.get("username").asString
                val skillsJson= jo.getAsJsonArray("skills")
                val xpList = List(skillsJson.size()) { i -> skillsJson[i].asJsonObject.get("xp").asDouble }
                val lvlList= List(skillsJson.size()) { i -> skillsJson[i].asJsonObject.get("lvl").asInt }
                val privilege= jo.get("privilege").asInt
                val combatLvl= calcCombatLevel(lvlList)
                loaded.add(PlayerSave(username, xpList, lvlList, privilege, combatLvl))
            } catch (e: Exception) {
                logger.error(e) { "Failed to load save: $path" }
                failed.add(path.fileName.toString())
            }
        }
        logger.info("Loaded ${loaded.size} players, failed ${failed.size} files")

        val sorted = if (skillId in 0 until skillsCount) loaded.sortedByDescending { it.xp[skillId] }
        else loaded.sortedByDescending { it.xp.sum() }

        return JsonObject().apply {
            if (skillId in 0 until skillsCount) addProperty("skill", skillId)
            else addProperty("skill", "overall")
            addProperty("countLoaded", loaded.size)
            addProperty("countFailed", failed.size)
            add("failedFiles", JsonArray().apply { failed.forEach { add(it) } })

            add("highscores", JsonArray().apply {
                sorted.take(limit).forEachIndexed { idx, ps ->
                    add(JsonObject().apply {
                        addProperty("rank", idx + 1)
                        addProperty("username", ps.username)
                        addProperty("privilege", ps.privilege)
                        addProperty("combatLvl", ps.combatLevel)
                        if (skillId in 0 until skillsCount) {
                            addProperty("xp", ps.xp[skillId])
                            addProperty("lvl", ps.lvl[skillId])
                        } else {
                            addProperty("totalXp", ps.xp.sum())
                            addProperty("lvl", ps.lvl.sum())
                        }
                    })
                }
            })
        }
    }
}
