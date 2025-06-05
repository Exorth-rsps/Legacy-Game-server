package org.alter.plugins.content.area.legacy.barrows

import org.alter.api.cfg.Items
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Player
import org.alter.plugins.content.area.legacy.barrows.Barrows

Barrows.BROTHERS.forEachIndexed { index, brother ->
    on_item_option(item = Items.SPADE, option = "dig") {
        if (player.tile.withinRadius(brother.mound, 1)) {
            val npc = Npc(brother.id, brother.crypt, world)
            world.spawn(npc)
            return@on_item_option
        }
    }

    on_npc_death(brother.id) {
        val killer = npc.damageMap.getMostDamage() as? Player ?: return@on_npc_death
        val flags = killer.attr[Barrows.PROGRESS_ATTR] ?: 0
        killer.attr[Barrows.PROGRESS_ATTR] = flags or (1 shl index)
    }
}
