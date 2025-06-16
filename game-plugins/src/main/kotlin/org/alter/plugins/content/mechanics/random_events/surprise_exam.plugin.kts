package org.alter.plugins.content.mechanics.random_events

import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs
import org.alter.game.model.entity.Npc
import org.alter.game.model.queue.QueueTask

on_npc_option(npc = Npcs.MR_MORDAUT, option = "talk-to") {
    if (npc.owner != player) {
        player.message("Mr. Mordaut ignores you.")
        return@on_npc_option
    }
    npc.timers.remove(IGNORE_EVENT_TIMER)
    player.queue { surpriseExamDialog(npc) }
}

suspend fun QueueTask.surpriseExamDialog(npc: Npc) {
    chatNpc("Well done on your studies! Accept this book.", npc = npc.id)
    player.inventory.add(Items.BOOK_OF_KNOWLEDGE)
    chatNpc("Use it wisely.", npc = npc.id)
    world.remove(npc)
}
