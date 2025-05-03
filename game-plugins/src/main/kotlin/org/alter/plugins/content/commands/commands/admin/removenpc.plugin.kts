package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.priv.Privilege

on_command("removenpc", Privilege.ADMIN_POWER, description = "Remove Npc under player") {
    val chunk = world.chunks.getOrCreate(player.tile)
    val npc = chunk.getEntities<Npc>(player.tile, EntityType.NPC).firstOrNull()
    if (npc != null) {
        world.remove(npc)
    } else {
        player.message("No NPC found in tile.")
    }
}