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
import kotlin.math.pow
import kotlin.math.pow
import kotlin.streams.toList

/**
 * HighScoresController leest highscores uit alle speler-savebestanden (offline én online).
 * Berekent display levels op basis van xp en combat level op basis van raw skill levels.
 * Routes: /highscores en /highscores/skill/:skillId
 */
class HighScoresController(
    private val req: Request,
    private val resp: Response,
    auth: Boolean
) : Controller(req, resp, auth) {

    private val logger = KotlinLogging.logger {}
    private val gson = Gson()
    private val skillsCount = 25
    private val maxLevel = 99

    // XP thresholds voor levels 1–99 volgens OSRS
    private val xpTable: List<Double> by lazy {
        val table = mutableListOf(0.0)
        var sum = 0.0
        for (lvl in 1..maxLevel) {
            val incr = floor(lvl + 300 * 2.0.pow(lvl / 7.0))
            sum += incr
            table.add(floor(sum / 4.0))
        }
        table
    }

    // Converteer xp naar level op basis van xpTable
    private fun xpToLevel(xp: Double): Int {
        for (lvl in 1 until xpTable.size) {
            if (xp < xpTable[lvl]) return lvl - 1
        }
        return maxLevel
    }

    // Bereken OSRS combat level uit raw skill levels
    private fun calcCombatLevel(rawLevels: List<Int>): Int {
        val att  = rawLevels.getOrNull(0) ?: 1
        val def  = rawLevels.getOrNull(1) ?: 1
        val str  = rawLevels.getOrNull(2) ?: 1
        val hp   = rawLevels.getOrNull(3) ?: 10
        val rng  = rawLevels.getOrNull(4) ?: 1
        val pray = rawLevels.getOrNull(5) ?: 1
        val mag  = rawLevels.getOrNull(6) ?: 1

        val base = 0.25 * (def + hp + floor(pray / 2.0))
        val melee = 0.325 * (att + str)
        val range = 0.325 * floor(rng * 1.5)
        val magic = 0.325 * floor(mag * 1.5)
        return floor(base + max(melee, max(range, magic))).toInt().coerceAtMost(126)
    }

    // Vind saves directory via relative paden
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

    override fun init(world: World): JsonObject {
        val skillId = req.params("skillId")?.toIntOrNull()
            ?: req.queryParams("skill")?.toIntOrNull()
            ?: -1
        val limit = req.queryParams("limit")?.toIntOrNull() ?: 10

        val savesDir = resolveSavesDir().also {
            if (it == null) logger.warn("Geen saves map gevonden, alleen online players.")
            else logger.info("Using savesDir: $it")
        }
        val stream = savesDir?.let { Files.list(it) }
            ?: java.util.stream.Stream.empty<Path>()

        data class PlayerSave(
            val username: String,
            val xp: List<Double>,
            val displayLvl: List<Int>,
            val privilege: Int,
            val combatLevel: Int
        )

        val loaded = mutableListOf<PlayerSave>()
        val failed = mutableListOf<String>()

        stream.filter { Files.isRegularFile(it) }
            .forEach { path ->
                try {
                    val jo = gson.fromJson(Files.newBufferedReader(path), JsonObject::class.java)
                    val username = jo.get("username").asString
                    val skillsArr = jo.getAsJsonArray("skills")

                    val xpList = List(skillsArr.size()) { i ->
                        skillsArr[i].asJsonObject.get("xp").asDouble
                    }
                    val rawLvlList = List(skillsArr.size()) { i ->
                        skillsArr[i].asJsonObject.get("lvl").asInt
                    }
                    val dispLvlList = xpList.map { xpToLevel(it) }
                    val privilege = jo.get("privilege").asInt
                    val combatLevel = calcCombatLevel(rawLvlList)

                    loaded.add(PlayerSave(username, xpList, dispLvlList, privilege, combatLevel))
                } catch (e: Exception) {
                    logger.error(e) { "Failed to load save: $path" }
                    failed.add(path.fileName.toString())
                }
            }

        logger.info("Loaded ${loaded.size} players, failed ${failed.size} files")

        val sorted = if (skillId in 0 until skillsCount) {
            loaded.sortedByDescending { it.xp[skillId] }
        } else {
            loaded.sortedByDescending { it.xp.sum() }
        }

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
                            addProperty("lvl", ps.displayLvl[skillId])
                        } else {
                            addProperty("totalXp", ps.xp.sum())
                            addProperty("lvl", ps.displayLvl.sum())
                        }
                    })
                }
            })
        }
    }
}
