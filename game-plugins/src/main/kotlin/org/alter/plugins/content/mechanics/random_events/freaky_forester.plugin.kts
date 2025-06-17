package org.alter.plugins.content.mechanics.random_events

import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs
import org.alter.game.model.entity.Npc
import org.alter.game.model.queue.QueueTask

private val LEDERHOSEN_REWARDS = intArrayOf(
    Items.LEDERHOSEN_TOP,
    Items.LEDERHOSEN_SHORTS,
    Items.LEDERHOSEN_HAT
)

on_npc_option(npc = Npcs.FREAKY_FORESTER_6748, option = "talk-to") {
    val eventNpc = player.getInteractingNpc()
    if (eventNpc.owner != player) {
        player.message("The forester doesn't seem to notice you.")
        return@on_npc_option
    }
    eventNpc.timers.remove(IGNORE_EVENT_TIMER)
    player.queue { foresterDialog(eventNpc) }
}

suspend fun QueueTask.foresterDialog(eventNpc: Npc) {
    chatNpc("Good job hunting! Take this.", npc = eventNpc.id)
    val reward = LEDERHOSEN_REWARDS.random()
    player.inventory.add(reward)
    chatNpc("Wear it with pride.", npc = eventNpc.id)
    world.remove(eventNpc)
}
