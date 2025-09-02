package org.alter.game.service

import org.alter.game.Server
import org.alter.game.model.PlayerUID
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.math.min

/**
 * Service that keeps a configurable number of AI accounts online.
 * It registers or unregisters placeholder [Player] instances on start
 * to ensure the count remains within the configured bounds.
 */
class AiPlayerService : Service {

    private lateinit var config: AiPlayerConfig
    private val aiPlayers = mutableListOf<Player>()

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        config = world.getService(AiPlayerConfig::class.java) ?: AiPlayerConfig().also {
            it.init(server, world, ServerProperties())
        }
    }

    override fun postLoad(server: Server, world: World) {
        maintain(world)
    }

    override fun bindNet(server: Server, world: World) { }

    override fun terminate(server: Server, world: World) {
        aiPlayers.forEach { world.unregister(it) }
        aiPlayers.clear()
    }

    /**
     * Ensure the amount of registered AI players is within the configured
     * [AiPlayerConfig.minOnline] and [AiPlayerConfig.maxOnline] bounds.
     */
    fun maintain(world: World) {
        if (!::config.isInitialized) return

        val current = aiPlayers.size
        if (current < config.minOnline) {
            val available = config.names.filter { name -> aiPlayers.none { it.username.equals(name, true) } }
            val needed = min(config.minOnline - current, available.size)
            available.take(needed).forEach { name ->
                val p = Player(world)
                p.uid = PlayerUID(name)
                p.username = name
                p.tile = world.gameContext.home
                world.register(p)
                aiPlayers.add(p)
            }
            if (needed > 0) {
                logger.info { "Registered $needed AI player(s)." }
            }
        } else if (current > config.maxOnline) {
            val removeCount = current - config.maxOnline
            repeat(removeCount) {
                val p = aiPlayers.removeAt(aiPlayers.lastIndex)
                world.unregister(p)
            }
            logger.info { "Unregistered $removeCount AI player(s)." }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}

