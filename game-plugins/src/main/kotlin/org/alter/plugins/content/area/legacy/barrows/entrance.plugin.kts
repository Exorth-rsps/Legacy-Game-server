package org.alter.plugins.content.area.legacy.barrows

import org.alter.api.cfg.Items
import org.alter.plugins.content.area.legacy.barrows.Barrows

on_item_option(item = Items.SPADE, "dig") {
    val loc = player.tile
    Barrows.BROTHERS.forEachIndexed { index, brother ->
        if (loc.withinRadius(brother.mound, 1)) {
            player.animate(830)
            player.teleport(brother.crypt)
            return@on_item_option
        }
    }
}
