package org.alter.plugins.content.area.legacy.asgarnian_ice_dungeon.objs

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */
on_obj_option(obj = Objs.LADDER, option = "climb-down", lineOfSightDistance = 1) {
    when (player.tile.regionId) {
        11829 -> {
            on_obj_option(obj = Objs.LADDER, option = "Open", lineOfSightDistance = 1) {
                if (!player.inventory.contains(Items.COLD_KEY)) {
                    player.message("You need a cold key to use this ladder.")
                    return@on_obj_option
                }
                player.moveTo(3007, 9550, player.tile.height)
            }

        }

    }
}
