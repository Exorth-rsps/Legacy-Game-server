package org.alter.plugins.content.mechanics.random_events

import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs
import org.alter.game.model.entity.Npc
import org.alter.game.model.queue.QueueTask

private val ZOMBIE_REWARDS = intArrayOf(
    Items.ZOMBIE_SHIRT,
    Items.ZOMBIE_TROUSERS,
    Items.ZOMBIE_MASK,
    Items.ZOMBIE_GLOVES,
    Items.ZOMBIE_BOOTS
)

on_npc_option(npc = Npcs.LEO, option = "talk-to") {
    val eventNpc = player.getInteractingNpc()
    if (eventNpc.owner != player) {
        player.message("Leo isn't paying attention to you.")
        return@on_npc_option
    }
    eventNpc.timers.remove(IGNORE_EVENT_TIMER)
    player.queue { gravediggerDialog(eventNpc) }
}

suspend fun QueueTask.gravediggerDialog(eventNpc: Npc) {
    chatNpc("Thanks for helping with the graves.", npc = eventNpc.id)
    chatPlayer("Happy to help!")
    val reward = ZOMBIE_REWARDS.random()
    player.inventory.add(reward)
    chatNpc("Here, take this as a reward.", npc = eventNpc.id)
    world.remove(eventNpc)
}
