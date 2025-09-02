package org.alter.plugins.service.ai

import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.service.Service
import org.alter.game.service.AiPlayerService
import org.alter.game.service.ai.AiLearningService
import org.alter.plugins.ai.AiPlayerController
import gg.rsmod.util.ServerProperties

class AiPlayerControllerService : Service {

    private val controllers = mutableListOf<AiPlayerController>()
    private var build: String = "starter"

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        build = serviceProperties.getOrDefault("build", build)
    }

    override fun postLoad(server: Server, world: World) {
        val aiService = world.getService(AiPlayerService::class.java) ?: return
        val learning = world.getService(AiLearningService::class.java) ?: return

        aiService.maintain(world)
        aiService.getPlayers().forEach { player ->
            learning.registerAiPlayer(player)
            controllers.add(AiPlayerController(world, player, learning, build))
        }

        world.queue {
            while (true) {
                controllers.forEach { it.tick() }
                wait(1)
            }
        }
    }

    override fun bindNet(server: Server, world: World) { }

    override fun terminate(server: Server, world: World) {
        controllers.clear()
    }
}
