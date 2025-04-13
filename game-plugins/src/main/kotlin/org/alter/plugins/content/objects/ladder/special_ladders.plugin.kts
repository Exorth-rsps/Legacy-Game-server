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
on_obj_option(obj = Objs.LADDER_17384, option = "Climb-Down") {
    when(player.tile.regionId) {
        10521 -> { //Wizardsguild
            player.moveTo(x = 3766, z = 1217, height = 0)
        }
        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.LADDER_17385, option = "Climb-Up") {
    when(player.tile.regionId) {
        14867 -> { //Wizardsguild
            player.moveTo(x = 2683, z = 1636, height = 0)
        }
        11925 -> {
            player.moveTo(x = 2997, z = 3410, height = 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.LADDER_41924, option = "Climb-Down") {
    when(player.tile.regionId) {
        10520 -> { //Bryophyta
            player.moveTo(x = 4037, z = 1240, height = 0)
            //player.message("Nothing interesting happens.")
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.LADDER_41925, option = "Climb-Up") {
    when(player.tile.regionId) {
        16147 -> { //Bryophyta
            player.moveTo(x = 2640, z = 1572, height = 0)
            //player.message("Nothing interesting happens.")
        }

        else -> player.message("Nothing interesting happens.")
    }
}
