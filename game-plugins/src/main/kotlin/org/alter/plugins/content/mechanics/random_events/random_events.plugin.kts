package org.alter.plugins.content.mechanics.random_events

import org.alter.game.model.timer.TimerKey
import org.alter.api.cfg.Npcs
import org.alter.plugins.content.combat.isAttacking
import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.mechanics.random_events.IGNORE_EVENT_TIMER
import org.alter.plugins.content.mechanics.random_events.spawnRandomEvent

private val RANDOM_EVENT_TIMER = TimerKey()

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

