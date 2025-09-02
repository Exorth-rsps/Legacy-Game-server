package org.alter.plugins.ai

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.alter.api.ext.player
import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.entity.Player
import java.nio.file.Files
import java.nio.file.Paths
import java.util.ArrayDeque

/**
 * Returns and removes the first element of this [ArrayDeque], or `null` if the deque is empty.
 */
private fun <T> ArrayDeque<T>.removeFirstOrNull(): T? = if (isEmpty()) null else removeFirst()

/**
 * Simple state driven controller for automated players.
 *
 * Loads training goals from [ai_builds.yml] and NPC definitions from
 * [data/cfg/npcs.csv] to determine how to navigate and act in the world.
 */
class AiPlayerController(
    private val world: World,
    private val player: Player,
    buildName: String
) {

    private val npcConfigs: Map<Int, String> = loadNpcConfigs()
    private val builds: Map<String, Build> = loadBuilds()
    private val goalQueue: ArrayDeque<TrainingGoal> =
        ArrayDeque(builds[buildName]?.goals ?: emptyList())

    private var state: State = State.IDLE

    fun tick() {
        when (val s = state) {
            State.IDLE -> {
                val next = goalQueue.removeFirstOrNull() ?: return
                state = State.MOVING(next)
                player.queue {
                    val pawn = this.player
                    pawn.walkTo(this, next.spawn.toTile())
                }
            }
            is State.MOVING -> {
                if (player.tile == s.goal.spawn.toTile()) {
                    state = State.ACTING(s.goal)
                    actOnGoal(s.goal)
                }
            }
            is State.ACTING -> state = State.IDLE
        }
    }

    private fun actOnGoal(goal: TrainingGoal) {
        goal.npc?.let { npcId ->
            val npc = world.npcs.firstOrNull { it.id == npcId && it.tile == goal.spawn.toTile() }
            if (npc != null) {
                player.attack(npc)
            }
        }
    }

    private fun loadNpcConfigs(): Map<Int, String> {
        val path = Paths.get("data/cfg/npcs.csv")
        if (!Files.exists(path)) return emptyMap()
        return Files.newBufferedReader(path).use { reader ->
            reader.lineSequence()
                .mapNotNull { line ->
                    val parts = line.split(",", limit = 2)
                    val id = parts.firstOrNull()?.toIntOrNull() ?: return@mapNotNull null
                    val desc = parts.getOrNull(1)?.trim() ?: ""
                    id to desc
                }.toMap()
        }
    }

    private fun loadBuilds(): Map<String, Build> {
        val path = Paths.get("ai_builds.yml")
        if (!Files.exists(path)) return emptyMap()
        val mapper = ObjectMapper(YAMLFactory())
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper.readValue(path.toFile())
    }

    data class Build(val goals: List<TrainingGoal> = emptyList())
    data class TrainingGoal(
        val skill: String,
        val target: Int,
        val npc: Int? = null,
        val obj: Int? = null,
        val spawn: Spawn
    )

    data class Spawn(val x: Int, val z: Int, val height: Int = 0) {
        fun toTile(): Tile = Tile(x, z, height)
    }

    private sealed class State {
        object IDLE : State()
        data class MOVING(val goal: TrainingGoal) : State()
        data class ACTING(val goal: TrainingGoal) : State()
    }
}

