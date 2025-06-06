package org.alter.game

import com.google.common.base.Stopwatch
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.entity.GroundItem
import org.alter.game.model.entity.Npc
import org.alter.game.model.skill.SkillSet
import org.alter.game.protocol.ClientChannelInitializer
import org.alter.game.service.GameService
import org.alter.game.service.rsa.RsaService
import org.alter.game.service.xtea.XteaKeyService
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KLogger
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

import io.github.oshai.kotlinlogging.KotlinLogging
import net.runelite.cache.fs.Store
import java.net.InetSocketAddress
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DecimalFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import org.alter.game.service.log.PublicChatLoggerService

/**
 * The [Server] is responsible for starting any and all games.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Server {

    /**
     * The properties specific to our API.
     */
    private val apiProperties = ServerProperties()

    private val acceptGroup = NioEventLoopGroup(2)

    private val ioGroup = NioEventLoopGroup(1)

    val bootstrap = ServerBootstrap()

    /**
     * Prepares and handles any API related logic that must be handled
     * before the game can be launched properly.
     */
    fun startServer(apiProps: Path) {
        Thread.setDefaultUncaughtExceptionHandler { t, e -> logger.error("Uncaught server exception in thread $t!", e) }
        val stopwatch = Stopwatch.createStarted()

        /*
         * Load the API property file.
         */
        apiProperties.loadYaml(apiProps.toFile())
        logger.info("Preparing ${getApiName()}...")

        /*
         * Inform the time it took to load the API related logic.
         */
        logger.info("${getApiName()} loaded up in ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms.")
    }

    /**
     * Prepares and handles any game related logic that was specified by the
     * user.
     *
     * Due to being decoupled from the API logic that will always be used, you
     * can start multiple servers with different game property files.
     */
    fun startGame(filestore: Path, gameProps: Path, packets: Path, blocks: Path, devProps: Path?): World {
        val stopwatch = Stopwatch.createStarted()
        val individualStopwatch = Stopwatch.createUnstarted()

        /*
         * Load the game property file.
         */
        val initialLaunch = Files.deleteIfExists(Paths.get("../first-launch"))
        val gameProperties = ServerProperties()
        val devProperties = ServerProperties()
        gameProperties.loadYaml(gameProps.toFile())
        if (devProps != null && Files.exists(devProps)) {
            devProperties.loadYaml(devProps.toFile())
        }
        logger.info("Loaded properties for ${gameProperties.get<String>("name")!!}.")

        /*
         * Create a game context for our configurations and services to run.
         */
        val autoBanOn = gameProperties.getOrDefault("autoBanEnabled", false)
        val autoBanMs = gameProperties.getOrDefault("autoBanIntervalMs", 1000).toLong()
        val autoIPBanOn = gameProperties.getOrDefault("autoIPBanEnabled", false)

        logger.info(" Auto-ban: enabled=$autoBanOn, ipBanEnabled=$autoIPBanOn, intervalMs=$autoBanMs")

        val gameContext = GameContext(
            initialLaunch = initialLaunch,
            name = gameProperties.get<String>("name")!!,
            revision = gameProperties.get<Int>("revision")!!,
            cycleTime = gameProperties.getOrDefault("cycle-time", 600),
            xpmultiplier = gameProperties.getOrDefault("xpmultiplier", 1.0),
            doublexp = gameProperties.getOrDefault("doublexp", false),

            playerLimit = gameProperties.getOrDefault("max-players", 2048),
            home = Tile(
                gameProperties.get<Int>("home-x")!!,
                gameProperties.get<Int>("home-z")!!,
                gameProperties.getOrDefault("home-height", 0)
            ),
            skillCount = gameProperties.getOrDefault("skill-count", SkillSet.DEFAULT_SKILL_COUNT),
            npcStatCount = gameProperties.getOrDefault("npc-stat-count", Npc.Stats.DEFAULT_NPC_STAT_COUNT),
            runEnergy = gameProperties.getOrDefault("run-energy", true),
            gItemPublicDelay = gameProperties.getOrDefault(
                "gitem-public-spawn-delay",
                GroundItem.DEFAULT_PUBLIC_SPAWN_CYCLES
            ),
            gItemDespawnDelay = gameProperties.getOrDefault("gitem-despawn-delay", GroundItem.DEFAULT_DESPAWN_CYCLES),
            preloadMaps = gameProperties.getOrDefault("preload-maps", false),
            autoBanEnabled    = autoBanOn,
            autoIPBanEnabled  = autoIPBanOn,
            autoBanIntervalMs = autoBanMs
        )

        val devContext = DevContext(
            debugExamines = devProperties.getOrDefault("debug-examines", false),
            debugObjects = devProperties.getOrDefault("debug-objects", false),
            debugButtons = devProperties.getOrDefault("debug-buttons", false),
            debugItemActions = devProperties.getOrDefault("debug-items", false),
            debugMagicSpells = devProperties.getOrDefault("debug-spells", false)
        )

        val world = World(gameContext, devContext)

        /*
         * Load the file store.
         */
        individualStopwatch.reset().start()
        world.filestore = Store(filestore.toFile())
        world.filestore.load()
        logger.info("Loaded filestore from path {} in {}ms.", filestore, individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /*
         * Load the definitions.
         */
        world.definitions.loadAll(world.filestore)

        /*
         * Load the services required to run the server.
         */
        world.loadServices(this, gameProperties)
        world.init()

        if (gameContext.preloadMaps) {
            /*
             * Preload region definitions.
             */
            world.getService(XteaKeyService::class.java)?.let { service ->
                world.definitions.loadRegions(world, world.chunks, service.validRegions)
            }
        }

        /*
         * Load the packets for the game.
         */
        world.getService(type = GameService::class.java)?.let { gameService ->
            individualStopwatch.reset().start()
            gameService.messageStructures.load(packets.toFile())
            gameService.messageEncoders.init()
            gameService.messageDecoders.init(gameService.messageStructures)
            logger.info("Loaded message codec and handlers in {}ms.", individualStopwatch.elapsed(TimeUnit.MILLISECONDS))
        }

        /*
         * Load the update blocks for the game.
         */
        individualStopwatch.reset().start()
        world.loadUpdateBlocks(blocks.toFile())
        logger.info("Loaded update blocks in {}ms.", individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /*
         * Load the privileges for the game.
         */
        individualStopwatch.reset().start()
        world.privileges.load(gameProperties)
        logger.info("Loaded {} privilege levels in {}ms.", world.privileges.size(), individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /*
         * Load the plugins for game content.
         */
        individualStopwatch.reset().start()
        world.plugins.init(
                server = this, world = world,
                jarPluginsDirectory = gameProperties.getOrDefault("plugin-packed-path", "../plugins"))
        logger.info("Loaded {} plugins in {}ms.", DecimalFormat().format(world.plugins.getPluginCount()), individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /*
         * Post load world.
         */
        world.postLoad()

        /*
         * Inform the time it took to load up all non-network logic.
         */
        logger.info("${gameProperties.get<String>("name")!!} double xp is ${gameProperties.get<String>("doublexp")!!}")
        logger.info("Auto-ban: enabled=${gameContext.autoBanEnabled}, intervalMs=${gameContext.autoBanIntervalMs}")
        logger.info("${gameProperties.get<String>("name")!!} loaded up in ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms.")

        /*
         * Set our bootstrap's groups and parameters.
         */
        val rsaService = world.getService(RsaService::class.java)
        val clientChannelInitializer = ClientChannelInitializer(revision = gameContext.revision,
                rsaExponent = rsaService?.getExponent(), rsaModulus = rsaService?.getModulus(),
                filestore = world.filestore, world = world)

        bootstrap.group(acceptGroup, ioGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(clientChannelInitializer)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        /*
         * Bind all service networks, if applicable.
         */
        world.bindServices(this)

        /*
         * Bind the game port.
         */
        val port = gameProperties.getOrDefault("game-port", 43594)
        val serverChannel = bootstrap.bind(InetSocketAddress(port)).sync()
        serverChannel.awaitUninterruptibly()
        logger.info("Now listening for incoming connections on port $port...")
        System.gc()
        logger.info("For commands, type `help` or `?` ")

        var input: String?
        do {
            val name = gameProperties.getOrDefault("name", "Alter")
            // Can add print("") here but keep in mind that it will merge with other lines.
            input = readLine() /** @TODO Don't know but readlnOrNull() shows that it does not exist. */
            if (input == null) {
                break
            }
            print("\u001B[32m$name\u001B[0m@Live:~$ ")
            val values = input.split(" ")
            val args = if (values.size > 1) values.slice(1 until values.size).filter { it.isNotEmpty() }.toTypedArray() else null
            val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))

            println("[ \u001B[32mCOMMAND\u001B[0m ] [ $currentTime ]: $input")
            println("${values}")
        } while (serverChannel.channel().isActive)


        return world
    }

    /**
     * Gets the API-specific org name.
     */
    fun getApiName(): String = apiProperties.getOrDefault("org", "Alter")

    companion object {
        val logger = KotlinLogging.logger {}
    }
}
