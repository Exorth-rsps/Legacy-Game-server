package org.alter.plugins.content.mechanics.random_events

import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs
import org.alter.game.model.entity.Npc
import org.alter.game.model.queue.QueueTask

private val SANDWICH_REWARDS = intArrayOf(
    Items.SANDWICH_LADY_HAT,
    Items.SANDWICH_LADY_TOP,
    Items.SANDWICH_LADY_BOTTOM,
    Items.BAGUETTE
)

on_npc_option(npc = Npcs.SANDWICH_LADY, option = "talk-to") {
    val eventNpc = player.getInteractingNpc()
    if (eventNpc.owner != player) {
        player.message("The Sandwich Lady is busy with someone else.")
        return@on_npc_option
    }
    eventNpc.timers.remove(IGNORE_EVENT_TIMER)
    player.queue { sandwichDialog(eventNpc) }
}

suspend fun QueueTask.sandwichDialog(eventNpc: Npc) {
    chatNpc("Fancy a snack? Here you go!", npc = eventNpc.id)
    val reward = SANDWICH_REWARDS.random()
    player.inventory.add(reward)
    chatNpc("Enjoy your treat.", npc = eventNpc.id)
    world.remove(eventNpc)
}
