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
        12184 -> { //Mining Guild
            player.moveTo(x = 3021, z = 3339, height = 0)
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

on_obj_option(obj = Objs.LADDER_23584, option = "Climb-Up") {
    when(player.tile.regionId) {
        11410 -> { //Shilo Gem mine
            player.moveTo(x = 3054, z = 9744, height = 0)
            //player.message("Nothing interesting happens.")
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.LADDER_18988, option = "Climb-Up") {
    when(player.tile.regionId) {
        12192 -> { //KBD
            player.moveTo(x = 3117, z = 3244, height = 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.LADDER_18990, option = "Climb-Up") {
    when(player.tile.regionId) {
        12192 -> { //Dragons
            player.moveTo(x = 3084, z = 3271, height = 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.LADDER_30367, option = "Climb-down") {
    when (player.tile.regionId) {
        12084 -> { // Mining Guild ladder
            if (player.getSkills().getCurrentLevel(Skills.MINING) < 60) {
                player.message("You need at least level 60 Mining to enter the Guild.")
                return@on_obj_option
            }
            player.moveTo(x = 3021, z = 9739, height = 0)
        }
        else -> {
            player.message("Nothing interesting happens.")
        }
    }
}
on_obj_option(obj = Objs.LADDER_2408, option = "Climb-Down") {
    when(player.tile.regionId) {
        11316 -> { //Chaos Druide
            player.moveTo(x = 1867, z = 4243, height = 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.LADDER_25844, option = "Climb-Up") {
    when(player.tile.regionId) {
        7490 -> { //Chaos Druide
            player.moveTo(x = 2819, z = 3374, height = 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}



