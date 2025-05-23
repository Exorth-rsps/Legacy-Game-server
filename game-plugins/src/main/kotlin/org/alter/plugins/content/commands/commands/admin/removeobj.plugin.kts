package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.priv.Privilege

on_command("removeobj", Privilege.ADMIN_POWER, description = "Remove object under player") {
    val chunk = world.chunks.getOrCreate(player.tile)
    val obj = chunk.getEntities<GameObject>(player.tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull()
    if (obj != null) {
        world.remove(obj)
    } else {
        player.message("No object found in tile.")
    }
}