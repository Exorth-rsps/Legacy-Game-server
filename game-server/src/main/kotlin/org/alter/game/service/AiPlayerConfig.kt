package org.alter.game.service

import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.Server
import org.alter.game.model.World
import java.io.File

class AiPlayerConfig : Service {

    var minOnline: Int = 0
    var maxOnline: Int = 0
    var names: List<String> = emptyList()

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val file = File("../data/cfg/ai_players.yml")
        val props = ServerProperties().loadYaml(file)
        minOnline = props.getOrDefault("min_online", 0)
        maxOnline = props.getOrDefault("max_online", 0)
        names = props.getOrDefault("names", emptyList())
        logger.info { "Loaded AI player config: minOnline=$minOnline, maxOnline=$maxOnline, names=${names.size}" }
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}
