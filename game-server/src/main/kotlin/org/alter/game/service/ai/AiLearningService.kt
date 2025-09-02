package org.alter.game.service.ai

import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.service.Service
import gg.rsmod.util.ServerProperties
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * Service that logs skill and combat events for AI controlled players and
 * periodically updates a simple Q-table model based on the collected data.
 *
 * Data is written to [data/ai] so it can be analysed offline or reused when
 * the server restarts.
 */
class AiLearningService : Service {

    private val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val dataDir: Path = Paths.get("data", "ai")
    private val eventLog: Path = dataDir.resolve("events.log")
    private val modelFile: Path = dataDir.resolve("qtable.dat")

    /**
     * Simple Q-table where key format is "state|action".
     */
    private val qTable = ConcurrentHashMap<String, Double>()

    /**
     * Tracks players that are flagged as AI controlled.
     */
    private val aiPlayers = mutableSetOf<Player>()

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        try {
            Files.createDirectories(dataDir)
            if (Files.exists(modelFile)) {
                Files.lines(modelFile).use { lines ->
                    lines.filter { it.contains('=') }.forEach { line ->
                        val parts = line.split('=')
                        if (parts.size == 2) {
                            qTable[parts[0]] = parts[1].toDoubleOrNull() ?: 0.0
                        }
                    }
                }
            }
        } catch (e: IOException) {
            System.err.println("AiLearningService could not prepare data directory: ${e.message}")
        }

        val interval = serviceProperties.getOrDefault("ai-learning-update-ms", 60_000L)
        scheduler.scheduleAtFixedRate({ updateModel() }, interval, interval, TimeUnit.MILLISECONDS)
    }

    override fun postLoad(server: Server, world: World) { /* no-op */ }

    override fun bindNet(server: Server, world: World) { /* no-op */ }

    override fun terminate(server: Server, world: World) {
        scheduler.shutdown()
        updateModel()
    }

    /**
     * Register or unregister players that should have their events tracked.
     */
    fun registerAiPlayer(player: Player) { aiPlayers.add(player) }
    fun unregisterAiPlayer(player: Player) { aiPlayers.remove(player) }

    /**
     * Log a skill related event for an AI player.
     */
    fun logSkillEvent(player: Player, skill: Int, xpGained: Double) {
        if (player !in aiPlayers) return
        val line = "skill,${player.username},$skill,$xpGained,${System.currentTimeMillis()}\n"
        try {
            Files.write(eventLog, line.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
        } catch (e: IOException) {
            System.err.println("AiLearningService failed to log skill event: ${e.message}")
        }
    }

    /**
     * Log a combat related event for an AI player.
     */
    fun logCombatEvent(player: Player, target: Pawn, damage: Int) {
        if (player !in aiPlayers) return
        val targetName = target::class.java.simpleName
        val line = "combat,${player.username},$targetName,$damage,${System.currentTimeMillis()}\n"
        try {
            Files.write(eventLog, line.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
        } catch (e: IOException) {
            System.err.println("AiLearningService failed to log combat event: ${e.message}")
        }
    }

    /**
     * Reads the event log and updates the in memory Q-table. After processing
     * the log file is cleared to avoid duplicate processing.
     */
    private fun updateModel() {
        if (!Files.exists(eventLog)) {
            persistModel()
            return
        }

        try {
            Files.lines(eventLog).use { lines ->
                lines.forEach { line ->
                    val parts = line.split(',')
                    if (parts.size < 5) return@forEach
                    val type = parts[0]
                    val state = parts[2]
                    val reward = parts[3].toDoubleOrNull() ?: return@forEach
                    val key = "$type|$state"
                    val current = qTable.getOrDefault(key, 0.0)
                    qTable[key] = current + reward
                }
            }
            // Clear log after processing
            Files.newBufferedWriter(eventLog, StandardOpenOption.TRUNCATE_EXISTING).use { }
        } catch (e: IOException) {
            System.err.println("AiLearningService could not update model: ${e.message}")
        }

        persistModel()
    }

    private fun persistModel() {
        try {
            val lines = qTable.entries.joinToString(System.lineSeparator()) { "${it.key}=${it.value}" }
            Files.write(modelFile, lines.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        } catch (e: IOException) {
            System.err.println("AiLearningService could not persist model: ${e.message}")
        }
    }
}

