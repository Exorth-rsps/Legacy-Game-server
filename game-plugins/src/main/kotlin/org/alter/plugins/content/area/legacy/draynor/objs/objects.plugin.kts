package org.alter.plugins.content.area.legacy.draynor.objs

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */
on_obj_option(obj = Objs.TRAPDOOR_6434, option = "open", lineOfSightDistance = 1) {
    when (player.tile.regionId) {
        12339 -> { //North (Dragons)
            player.moveTo(3017, 10248, player.tile.height)
        }

        12338 -> { //East (KBD)
            player.moveTo(3069, 10257, player.tile.height)
        }

        else -> player.message("Nothing interesting happens.")
    }
}

on_obj_option(obj = Objs.LOGS_5581, option = "Take-axe") {
    player.inventory.add(item = Items.BRONZE_AXE,1)
    player.message("You have taken the bronze axe.")
}
