package org.alter.game.service

import org.alter.game.Server
import org.alter.game.model.PlayerUID
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import org.alter.game.service.GameService
import org.alter.game.service.ai.AiLearningService
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.math.min
import java.lang.reflect.Constructor
import java.lang.reflect.Method

/**
 * Service that keeps a configurable number of AI accounts online.
 * It registers or unregisters placeholder [Player] instances on start
 * to ensure the count remains within the configured bounds.
 */
class AiPlayerService : Service {

    private lateinit var config: AiPlayerConfig
    private val aiPlayers = mutableListOf<Player>()
    private lateinit var learning: AiLearningService
    private var controllerCtor: Constructor<*>? = null
    private var tickMethod: Method? = null
    private val controllers = mutableMapOf<Player, Any>()

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        config = world.getService(AiPlayerConfig::class.java) ?: AiPlayerConfig().also {
            it.init(server, world, ServerProperties())
        }
        learning = world.getService(AiLearningService::class.java) ?: AiLearningService()
        try {
            val clazz = Class.forName("org.alter.plugins.ai.AiPlayerController")
            controllerCtor = clazz.getConstructor(World::class.java, Player::class.java, AiLearningService::class.java, String::class.java)
            tickMethod = clazz.getMethod("tick")
        } catch (e: Exception) {
            logger.warn { "AiPlayerController not found; AI players will be idle." }
        }
    }

    override fun postLoad(server: Server, world: World) {
        maintain(world)
        scheduleTick(world)
    }

    override fun bindNet(server: Server, world: World) { }

    override fun terminate(server: Server, world: World) {
        aiPlayers.forEach {
            controllers.remove(it)
            learning.unregisterAiPlayer(it)
            world.unregister(it)
        }
        aiPlayers.clear()
        controllers.clear()
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
                learning.registerAiPlayer(p)
                controllerCtor?.let { ctor ->
                    runCatching { controllers[p] = ctor.newInstance(world, p, learning, "starter") }
                        .onFailure { e -> logger.error(e) { "Failed to create AI controller for ${'$'}name" } }
                }
            }
            if (needed > 0) {
                logger.info { "Registered $needed AI player(s)." }
            }
        } else if (current > config.maxOnline) {
            val removeCount = current - config.maxOnline
            repeat(removeCount) {
                val p = aiPlayers.removeAt(aiPlayers.lastIndex)
                controllers.remove(p)
                learning.unregisterAiPlayer(p)
                world.unregister(p)
            }
            logger.info { "Unregistered $removeCount AI player(s)." }
        }
    }

    private fun scheduleTick(world: World) {
        val game = world.getService(GameService::class.java) ?: return
        game.submitGameThreadJob {
            tickControllers()
            scheduleTick(world)
        }
    }

    private fun tickControllers() {
        val method = tickMethod ?: return
        controllers.values.forEach { ctrl ->
            runCatching { method.invoke(ctrl) }
                .onFailure { e -> logger.error(e) { "Error ticking AI controller" } }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}

