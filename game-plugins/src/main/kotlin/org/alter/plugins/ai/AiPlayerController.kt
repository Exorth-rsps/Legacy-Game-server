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
import org.alter.game.service.ai.AiLearningService
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.random.Random

/**
 * Simple state driven controller for automated players.
 *
 * Loads training goals from [ai_builds.yml] and NPC definitions from
 * [data/cfg/npcs.csv] to determine how to navigate and act in the world.
 */
class AiPlayerController(
    private val world: World,
    private val player: Player,
    private val learning: AiLearningService,
    buildName: String
) {

    private val npcConfigs: Map<Int, String> = loadNpcConfigs()
    private val builds: Map<String, Build> = loadBuilds()
    private val goals: List<TrainingGoal> = builds[buildName]?.goals ?: emptyList()

    private var state: State = State.IDLE

    fun tick() {
        when (val s = state) {
            State.IDLE -> {
                val next = chooseNextGoal() ?: return
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
            is State.ACTING -> {
                learning.logGoalEvent(player, s.goal.skill, 1.0)
                state = State.IDLE
            }
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

    private fun chooseNextGoal(): TrainingGoal? {
        if (goals.isEmpty()) return null
        val epsilon = 0.1
        return if (Random.nextDouble() < epsilon) {
            goals.random()
        } else {
            goals.maxByOrNull { learning.getGoalValue(it.skill) } ?: goals.random()
        }
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

