package org.alter.game.task.parallel

import org.alter.game.model.World
import org.alter.game.model.entity.Pawn
import org.alter.game.service.GameService
import org.alter.game.sync.SynchronizationTask
import org.alter.game.sync.task.*
import org.alter.game.task.GameTask

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.ExecutorService
import java.util.concurrent.Phaser

/**
 * A [GameTask] that is responsible for sending [Pawn]
 * data to [Pawn]s in parallel, using a Phaser to
 * coordinate synchronization stages.
 */
class ParallelSynchronizationTask(private val executor: ExecutorService) : GameTask {

    /**
     * Phaser to wait for all parties at each synchronization phase.
     */
    private val phaser = Phaser(1)

    override fun execute(world: World, service: GameService) {
        // Freeze collections to avoid concurrent modification issues
        val players = world.players.toList()
        val playerCount = players.size
        val npcs = world.npcs.entries.toList()
        val npcCount = npcs.size

        val npcSync = NpcSynchronizationTask(npcs)

        // Player Pre-Synchronization
        phaser.bulkRegister(playerCount)
        players.forEach { player ->
            submit(phaser, executor, player, PlayerPreSynchronizationTask)
        }
        phaser.arriveAndAwaitAdvance()

        // NPC Pre-Synchronization
        phaser.bulkRegister(npcCount)
        npcs.forEach { entry ->
            submit(phaser, executor, entry, NpcPreSynchronizationTask)
        }
        phaser.arriveAndAwaitAdvance()

        // Player Synchronization
        phaser.bulkRegister(playerCount)
        players.forEach { player ->
            if (player.entityType.isHumanControlled && player.initiated) {
                submit(phaser, executor, player, PlayerSynchronizationTask)
            } else {
                phaser.arriveAndDeregister()
            }
        }
        phaser.arriveAndAwaitAdvance()

        // NPC Synchronization per player
        phaser.bulkRegister(playerCount)
        players.forEach { player ->
            if (player.entityType.isHumanControlled && player.initiated) {
                submit(phaser, executor, player, npcSync)
            } else {
                phaser.arriveAndDeregister()
            }
        }
        phaser.arriveAndAwaitAdvance()

        // Player Post-Synchronization
        phaser.bulkRegister(playerCount)
        players.forEach { player ->
            submit(phaser, executor, player, PlayerPostSynchronizationTask)
        }
        phaser.arriveAndAwaitAdvance()

        // NPC Post-Synchronization
        phaser.bulkRegister(npcCount)
        npcs.forEach { entry ->
            submit(phaser, executor, entry, NpcPostSynchronizationTask)
        }
        phaser.arriveAndAwaitAdvance()
    }

    private fun <T : Pawn> submit(
        phaser: Phaser,
        executor: ExecutorService,
        pawn: T,
        task: SynchronizationTask<T>
    ) {
        executor.execute {
            try {
                task.run(pawn)
            } catch (e: ArrayIndexOutOfBoundsException) {
                logger.error(e) { "OOB in ${task::class.java.simpleName} for pawn $pawn: ${e.message}" }
                throw e
            } catch (e: Exception) {
                logger.error(e) { "Error in ${task::class.java.simpleName} for pawn $pawn: ${e.message}" }
            } finally {
                phaser.arriveAndDeregister()
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
