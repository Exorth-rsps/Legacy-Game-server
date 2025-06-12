package org.alter.plugins.content.area.legacy.barrows

import org.alter.api.cfg.Objs
import org.alter.game.model.entity.Npc
import org.alter.plugins.content.drops.DropTableFactory
import org.alter.plugins.content.drops.DropTableType
import org.alter.plugins.content.area.legacy.barrows.Barrows

on_obj_option(obj = Objs.CHEST_20723, option = "open") {
    val completed = (1 shl Barrows.BROTHERS.size) - 1
    val progress = player.attr[Barrows.PROGRESS_ATTR] ?: 0
    val missing = Barrows.BROTHERS.indices.filter { progress and (1 shl it) == 0 }

    if (progress == completed) {
        player.attr[Barrows.PROGRESS_ATTR] = 0
        val drops = DropTableFactory.getDrop(player, Barrows.CHEST, DropTableType.CHEST)?.shuffled()?.take(3)
        drops?.forEach { player.inventory.add(it) }
        val last = player.attr[Barrows.LAST_BROTHER_ATTR]
        if (last != null) {
            DropTableFactory.getDrop(player, Barrows.BROTHERS[last].id, DropTableType.CHEST)
                ?.firstOrNull()?.let { player.inventory.add(it) }
        }
        player.attr.remove(Barrows.LAST_BROTHER_ATTR)
        player.message("You loot the chest and feel a strange power fade.")
        return@on_obj_option
    }

    if (missing.size == 1) {
        val index = missing.first()
        val brother = Barrows.BROTHERS[index]
        val spawned = world.npcs.any { it.owner == player && it.id == brother.id }
        if (!spawned) {
            val npc = Npc(player, brother.id, player.tile.transform(1, 0), world)
            npc.respawns = false
            world.spawn(npc)
            player.message("A Barrows brother emerges from the shadows!")
        }
        return@on_obj_option
    }

    player.message("The chest is sealed shut.")
}
