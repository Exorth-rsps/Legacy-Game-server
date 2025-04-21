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

        get("/players") {
                req, res -> Gson().toJson(OnlinePlayersController(req, res, false).init(world))
        }
                get("/players/stream") { req, res ->
                        res.type("text/event-stream")
                        res.header("Cache-Control", "no-cache")
                        val out = res.raw().writer
                        val gson = Gson()

                        Timer(true).scheduleAtFixedRate(object: TimerTask() {
                                override fun run() {
                                        val json = gson.toJson(
                                                OnlinePlayersController(req, res, false).init(world)
                                                    )
                                        out.write("data: $json\n\n")
                                        out.flush()
                                    }
                            }, 0, 5000)
            
                        // Houd de verbinding open
                        CountDownLatch(1).await()
                        ""
                    }

        get("/player/:name") {
                req, res -> Gson().toJson(PlayerController(req, res, false).init(world))
        }
    }
}