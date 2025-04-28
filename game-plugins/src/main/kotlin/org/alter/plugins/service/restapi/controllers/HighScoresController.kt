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
import kotlin.streams.toList

/**
 * HighScoresController leest highscores uit alle speler-savebestanden (offline én online).
 * Berekent display levels op basis van xp en combat level op basis van dezelfde display levels.
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

    // Hardcoded XP thresholds volgens OSRS-experience tabel (levels 1–99)
    private val xpTable: List<Double> = listOf(
        0.0, 0.0, 83.0, 174.0, 276.0, 388.0, 512.0, 650.0, 801.0, 969.0,
        1154.0, 1358.0, 1584.0, 1833.0, 2107.0, 2411.0, 2746.0, 3115.0, 3523.0, 3973.0,
        4470.0, 5018.0, 5624.0, 6291.0, 7028.0, 7842.0, 8740.0, 9730.0, 10824.0, 12031.0,
        13363.0, 14833.0, 16456.0, 18247.0, 20224.0, 22406.0, 24815.0, 27473.0, 30408.0, 33648.0,
        37224.0, 41171.0, 45529.0, 50339.0, 55649.0, 61512.0, 67983.0, 75127.0, 83014.0, 91921.0,
        101333.0, 111945.0, 123660.0, 136594.0, 150872.0, 166636.0, 184040.0, 203254.0, 224466.0, 247886.0,
        273742.0, 302288.0, 333804.0, 368599.0, 407015.0, 449428.0, 496254.0, 547953.0, 605032.0, 668051.0,
        737627.0, 814445.0, 899257.0, 992895.0, 1096278.0, 1210421.0, 1336443.0, 1475581.0, 1629200.0, 1798808.0,
        1986068.0, 2192818.0, 2421087.0, 2673114.0, 2951373.0, 3258594.0, 3597792.0, 3972294.0, 4385776.0, 4842295.0,
        5346332.0, 5902831.0, 6517253.0, 7195629.0, 7944614.0, 8771558.0, 9684577.0, 10692629.0, 11805606.0, 13034431.0
    )

    // Converteer xp naar level op basis van xpTable
    private fun xpToLevel(xp: Double): Int {
        for (level in maxLevel downTo 1) {
            if (xp >= xpTable[level]) return level
        }
        return 1
    }

    // Bereken OSRS combat level uit display levels
    private fun calcCombatLevel(levels: List<Int>): Int {
        val att  = levels.getOrNull(0) ?: 1
        val def  = levels.getOrNull(1) ?: 1
        val str  = levels.getOrNull(2) ?: 1
        val hp   = levels.getOrNull(3) ?: 10
        val rng  = levels.getOrNull(4) ?: 1
        val pray = levels.getOrNull(5) ?: 1
        val mag  = levels.getOrNull(6) ?: 1

        val base  = 0.25 * (def + hp + floor(pray / 2.0))
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
            if (it == null) logger.warn("Geen saves map gevonden, fallback naar online-only.")
            else logger.info("Using savesDir: $it")
        }
        val filesStream = savesDir?.let { Files.list(it) }
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

        filesStream.filter { Files.isRegularFile(it) }.forEach { path ->
            try {
                val jo = gson.fromJson(Files.newBufferedReader(path), JsonObject::class.java)
                val username  = jo.get("username").asString
                val skillsArr = jo.getAsJsonArray("skills")

                // XP values and derived display levels
                val xpList      = List(skillsArr.size()) { i -> skillsArr[i].asJsonObject.get("xp").asDouble }
                val dispLvlList = xpList.map { xpToLevel(it) }
                val privilege   = jo.get("privilege").asInt
                val combatLvl   = calcCombatLevel(dispLvlList)

                loaded.add(PlayerSave(username, xpList, dispLvlList, privilege, combatLvl))
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
