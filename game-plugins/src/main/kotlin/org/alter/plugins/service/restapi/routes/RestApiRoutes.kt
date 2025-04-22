package org.alter.plugins.service.restapi.routes

import com.google.gson.Gson
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.CountDownLatch
import org.alter.game.model.World
import org.alter.plugins.service.restapi.controllers.CommandsController
import org.alter.plugins.service.restapi.controllers.OnlinePlayersController
import org.alter.plugins.service.restapi.controllers.PlayerController
import spark.Spark.*

class RestApiRoutes {
    fun init(world: World, auth: Boolean) {
        get("/players/stream") { req, res ->
            // 1) SSE‑headers
            res.type("text/event-stream;charset=UTF-8")
            res.header("Cache-Control", "no-cache")
            res.header("Connection", "keep-alive")
            // (optie) voorkom buffer:
            res.raw().bufferSize = 0

            val out = res.raw().writer
            val gson = Gson()

            // 2) Eerste ping/comment event direct flushen
            out.write("retry: 10000\n")     // adviseer 10s reconnect
            out.write(": connected\n\n")    // een comment event
            out.flush()

            // 3) Daarna periodiek data sturen
            Timer(true).scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val json = gson.toJson(
                        OnlinePlayersController(req, res, false).init(world)
                    )
                    out.write("data: $json\n\n")
                    out.flush()
                }
            }, 0, 5000)

            // 4) Houd de thread open
            CountDownLatch(1).await()
            "" // unreachable
        }


        get("/player/:name") {
                req, res -> Gson().toJson(PlayerController(req, res, false).init(world))
        }
    }
}