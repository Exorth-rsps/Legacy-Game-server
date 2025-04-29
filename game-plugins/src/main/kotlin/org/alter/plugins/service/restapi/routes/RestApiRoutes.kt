package org.alter.plugins.service.restapi.routes

import com.google.gson.Gson
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.CountDownLatch
import org.alter.game.model.World
import org.alter.plugins.service.restapi.controllers.OnlinePlayersController
import org.alter.plugins.service.restapi.controllers.PlayerController
import org.alter.plugins.service.restapi.controllers.OfflinePlayerController
import org.alter.plugins.service.restapi.controllers.HighScoresController
import spark.Spark.*

class RestApiRoutes {
    fun init(world: World, auth: Boolean) {
        // SSE-endpoint voor live updates van online spelers
        get("/players/stream") { req, res ->
            // 1) SSE-headers
            res.type("text/event-stream;charset=UTF-8")
            res.header("Cache-Control", "no-cache")
            res.header("Connection", "keep-alive")
            res.raw().bufferSize = 0

            val out = res.raw().writer
            val gson = Gson()
            val timer = Timer(true)
            val latch = CountDownLatch(1)

            // 2) Eerste ping/comment event om verbinding levend te houden
            out.write("retry: 10000\n")      // client retry na 10s bij disconnect
            out.write(": connected\n\n")    // comment event ter bevestiging
            out.flush()

            // 3) Periodiek online spelers-data sturen
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    try {
                        val json = gson.toJson(
                            OnlinePlayersController(req, res, auth).init(world)
                        )
                        out.write("data: $json\n\n")
                        out.flush()
                    } catch (t: Exception) {
                        // Client weggevallen of andere I/O-fout
                        timer.cancel()
                        latch.countDown()
                    }
                }
            }, 0, 5_000)

            // 4) Houd de thread open tot de client disconnect
            latch.await()
            "" // unreachable, maar vereist door Spark
        }

        // Eenvoudig JSON-endpoint voor individuele speler
        get("/player/:name") { req, res ->
            res.type("application/json")
            Gson().toJson(
                PlayerController(req, res, auth).init(world)
            )
        }

        // Highscores - overall
        get("/highscores") { req, res ->
            res.type("application/json")
            Gson().toJson(
                HighScoresController(req, res, auth).init(world)
            )
        }

        // Highscores - per skill
        get("/highscores/skill/:skillId") { req, res ->
            res.type("application/json")
            Gson().toJson(
                HighScoresController(req, res, auth).init(world)
            )
        }
        get("/offline-player/:name") { req, res ->
            res.type("application/json")
            Gson().toJson(OfflinePlayerController(req, res, auth).init(world))
        }

    }
}
