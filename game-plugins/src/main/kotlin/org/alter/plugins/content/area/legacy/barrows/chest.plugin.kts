package org.alter.plugins.content.area.legacy.barrows

import org.alter.api.cfg.Objs
import org.alter.api.cfg.Items
import org.alter.plugins.content.area.legacy.barrows.Barrows

on_obj_option(obj = Objs.CHEST_20723, option = "open") {
    val completed = (1 shl Barrows.BROTHERS.size) - 1
    val progress = player.attr[Barrows.PROGRESS_ATTR] ?: 0
    if (progress != completed) {
        player.message("The chest is empty.")
        return@on_obj_option
    }
    player.attr[Barrows.PROGRESS_ATTR] = 0
    player.inventory.add(Items.BOLT_RACK, 50)
    player.inventory.add(Items.DEATH_RUNE, 100)
    player.inventory.add(Items.BLOOD_RUNE, 100)
    player.message("You loot the chest and feel a strange power fade.")
}
