package org.alter.plugins.content.mechanics.random_events

import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs
import org.alter.game.model.entity.Npc
import org.alter.game.model.queue.QueueTask

on_npc_option(npc = Npcs.GENIE, option = "talk-to") {
    if (npc.owner != player) {
        player.message("The genie ignores you.")
        return@on_npc_option
    }
    npc.timers.remove(IGNORE_EVENT_TIMER)
    player.queue { genieDialog(npc) }
}

suspend fun QueueTask.genieDialog(eventNpc: Npc) {
    chatNpc("Greetings adventurer, I have a gift for you.", npc = eventNpc.id)
    chatPlayer("Thanks!")
    player.inventory.add(Items.LAMP)
    chatNpc("Rub the lamp to gain some experience.", npc = eventNpc.id)
    world.remove(eventNpc)
}
