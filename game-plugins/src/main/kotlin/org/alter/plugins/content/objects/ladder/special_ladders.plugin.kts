package org.alter.plugins.content.objects.ladder

import org.alter.api.cfg.Objs

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */
on_obj_option(obj = Objs.LADDER_26118, option = "Climb-Up") {
    when(player.tile.regionId) {
        10779 -> { //Seers Village
            player.moveTo(x = 2749, z = 1743, height = 3)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.TRAPDOOR_26119, option = "Climb-Down") {
    when(player.tile.regionId) {
        10779 -> { //Seers Village
            player.moveTo(x = 2749, z = 1743, height = 1)
        }
        else -> player.message("Nothing interesting happens.")
    }
}

