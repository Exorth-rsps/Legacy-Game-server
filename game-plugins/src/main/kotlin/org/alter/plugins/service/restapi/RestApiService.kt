package org.alter.plugins.service.restapi

import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.service.Service
import org.alter.plugins.service.restapi.routes.CorsRoute
import org.alter.plugins.service.restapi.routes.RestApiRoutes
import gg.rsmod.util.ServerProperties
import spark.Spark

class RestApiService : Service {
    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        // 1) HTTP-poort instellen (default 4567, of uit je properties)
        val httpPort = serviceProperties.getOrDefault("port", "4567").toInt()
        Spark.port(httpPort)

        // 2) Serve een lege robots.txt om die 404-warning weg te halen
        Spark.get("/robots.txt") { req, res ->
            res.type("text/plain")
            "User-agent: *\nDisallow:"
        }

        // 3) Je bestaande CORS-configuratie
        CorsRoute(
            serviceProperties.getOrDefault("origin", "*"),
            serviceProperties.getOrDefault("methods", "GET, POST"),
            serviceProperties.getOrDefault("headers", "X-PINGOTHER, Content-Type")
        )

        // 4) Je eigen API-routes
        RestApiRoutes().init(world, serviceProperties.getOrDefault("auth", false))
    }

    override fun postLoad(server: Server, world: World) {}
    override fun bindNet(server: Server, world: World) {}

    override fun terminate(server: Server, world: World) {
        Spark.stop()
    }
}
