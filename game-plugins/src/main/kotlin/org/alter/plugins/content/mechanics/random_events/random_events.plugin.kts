package org.alter.plugins.content.mechanics.random_events

import org.alter.game.model.timer.TimerKey
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Player
import org.alter.game.model.Tile
import org.alter.api.cfg.Npcs
import org.alter.game.fs.def.NpcDef
import org.alter.plugins.content.combat.isAttacking
import org.alter.plugins.content.combat.isBeingAttacked

private val RANDOM_EVENT_TIMER = TimerKey()
private val IGNORE_EVENT_TIMER = TimerKey()

private const val IGNORE_DELAY = 100

private val EVENTS = intArrayOf(
    Npcs.GENIE,
    Npcs.LEO,
    Npcs.FREAKY_FORESTER_6748,
    Npcs.SANDWICH_LADY,
    Npcs.MR_MORDAUT
)

private const val MIN_DELAY = 3000
private const val MAX_DELAY = 6000

on_login {
    player.timers[RANDOM_EVENT_TIMER] = world.random(MIN_DELAY..MAX_DELAY)
}

on_logout {
    player.timers.remove(RANDOM_EVENT_TIMER)
}

on_timer(RANDOM_EVENT_TIMER) {
    if (player.isAttacking() || player.isBeingAttacked()) {
        player.timers[RANDOM_EVENT_TIMER] = world.random(MIN_DELAY..MAX_DELAY)
        return@on_timer
    }
    spawnRandomEvent(player)
    player.timers[RANDOM_EVENT_TIMER] = world.random(MIN_DELAY..MAX_DELAY)
}

on_timer(IGNORE_EVENT_TIMER) {
    val owner = npc.owner
    if (npc.isSpawned() && owner != null && owner.isOnline) {
        owner.message("${npc.def.name} grows impatient and teleports you home.")
        owner.moveTo(world.gameContext.home)
    }
    world.remove(npc)
}

fun spawnRandomEvent(p: Player) {
    val npcId = EVENTS.random()
    val tile = p.tile.transform(1, 0)
    val npc = Npc(npcId, tile, world)
    npc.owner = p
    npc.respawns = false
    npc.timers[IGNORE_EVENT_TIMER] = IGNORE_DELAY
    world.spawn(npc)
    val name = world.definitions.get(NpcDef::class.java, npcId).name
    p.message("A random event has appeared: $name. Talk to them or you'll be sent home!")
}
