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
    if (npc.owner != player) {
        player.message("The forester doesn't seem to notice you.")
        return@on_npc_option
    }
    npc.timers.remove(IGNORE_EVENT_TIMER)
    player.queue { foresterDialog(npc) }
}

suspend fun QueueTask.foresterDialog(npc: Npc) {
    chatNpc("Good job hunting! Take this.", npc = npc.id)
    val reward = LEDERHOSEN_REWARDS.random()
    player.inventory.add(reward)
    chatNpc("Wear it with pride.", npc = npc.id)
    world.remove(npc)
}
