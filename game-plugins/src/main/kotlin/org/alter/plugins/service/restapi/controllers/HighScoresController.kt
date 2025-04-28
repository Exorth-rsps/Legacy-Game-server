package org.alter.plugins.service.restapi.controllers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.model.World
import org.alter.game.model.entity.Player            // ← juiste Player
import spark.Request
import spark.Response

class HighScoresController(
    private val req: Request,
    private val resp: Response,
    auth: Boolean
) : Controller(req, resp, auth) {

    private val logger = KotlinLogging.logger {}

    override fun init(world: World): JsonObject {
        // 1) Skill-id uit path of query (?skill=…)
        val skillId = req.params("skillId")?.toIntOrNull()
            ?: req.queryParams("skill")?.toIntOrNull()
            ?: -1

        // 2) Hoeveel topscores (default 10)
        val limit = req.queryParams("limit")?.toIntOrNull() ?: 10

        // 3) PawnList → gewone Kotlin List<Player>
        val playerList = mutableListOf<Player>().apply {
            world.players.forEach(this::add)
        }

        // 4) Sorteren: per-skill xp of total xp
        val sorted = if (skillId in 0..22) {
            playerList.sortedByDescending { p ->
                p.getSkills().get(skillId).xp
            }
        } else {
            playerList.sortedByDescending { p ->
                // som van alle xp's
                (0..22).sumOf { idx -> p.getSkills().get(idx).xp }
            }
        }

        // 5) JSON-response opbouwen
        return JsonObject().apply {
            addProperty(
                "skill",
                if (skillId in 0..22) skillId.toString() else "overall"
            )
            addProperty("count", playerList.size)
            addProperty("limit", limit)

            val hsArr = JsonArray().apply {
                sorted.take(limit).forEachIndexed { idx, p ->
                    add(
                        JsonObject().apply {
                            addProperty("rank", idx + 1)
                            addProperty("username", p.username)

                            if (skillId in 0..22) {
                                val sk = p.getSkills().get(skillId)
                                addProperty("xp", sk.xp)
                                addProperty("lvl", sk.currentLevel)
                            } else {
                                val totalXp = (0..22).sumOf { i ->
                                    p.getSkills().get(i).xp
                                }
                                addProperty("totalXp", totalXp)
                            }
                        }
                    )
                }
            }
            add("highscores", hsArr)
        }
    }
}
