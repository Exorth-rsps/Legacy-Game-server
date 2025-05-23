package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.priv.Privilege
import org.alter.game.service.game.ItemMetadataService

/**
 * @author CloudS3c
 */
on_command("reloaditems", Privilege.ADMIN_POWER, description = "Reload all itemdefs") {
    world.getService(ItemMetadataService::class.java)!!.loadAll(world)
    player.message("All items were reloaded. Defs: ${world.definitions.getCount(ItemDef::class.java)} Took: ${world.getService(ItemMetadataService::class.java)!!.ms} ms.")
}