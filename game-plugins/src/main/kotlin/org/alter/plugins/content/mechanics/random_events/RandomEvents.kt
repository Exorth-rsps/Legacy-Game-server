package org.alter.plugins.content.mechanics.random_events

import org.alter.api.cfg.Npcs
import org.alter.game.fs.def.NpcDef
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Player
import org.alter.game.model.timer.TimerKey

/**
 * Utilities for random events.
 */
val IGNORE_EVENT_TIMER = TimerKey()

private const val IGNORE_DELAY = 100

private val EVENTS = intArrayOf(
    Npcs.GENIE,
    Npcs.LEO,
    Npcs.FREAKY_FORESTER_6748,
    Npcs.SANDWICH_LADY
)

fun spawnRandomEvent(player: Player, npcId: Int = EVENTS.random()) {
    val world = player.world
    val tile = player.tile.transform(1, 0)
    val npc = Npc(npcId, tile, world)
    npc.owner = player
    npc.respawns = false
    npc.timers[IGNORE_EVENT_TIMER] = IGNORE_DELAY
    world.spawn(npc)
    val name = world.definitions.get(NpcDef::class.java, npcId).name
    player.message("A random event has appeared: $name. Talk to them or you'll be sent home!")
}
