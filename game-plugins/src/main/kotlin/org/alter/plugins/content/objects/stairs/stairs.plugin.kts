package org.alter.plugins.content.objects.stairs
/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */

on_obj_option(obj = Objs.STAIRCASE_15645, option = "climb-Up") {
    when(player.tile.height) {
        1 -> {
            player.moveTo(x = 2680, z = 1637, 2)
        }
        else ->  player.moveTo(x = 2680, z = 1642, 1)
    }
}
on_obj_option(obj = Objs.STAIRCASE_15648, option = "climb-down") {
    when(player.tile.height) {
        1 -> {
            player.moveTo(x = 2681, z = 1638, 0)
        }
        else ->  player.moveTo(x = 2680, z = 1633, 1)
    }
}

on_obj_option(obj = Objs.STAIRCASE_15653, option = "Climb-Up") {
    when(player.tile.regionId) {
        11032 -> { // Legendsguild
            player.moveTo(x = 2767, z = 1596, 1)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.STAIRCASE_15654, option = "Climb-Down") {
    when(player.tile.regionId) {
        11032 -> { // Legendsguild
            player.moveTo(x = 2767, z = 1592, 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.STAIRCASE_25801, option = "Climb-Down") {
    when(player.tile.regionId) {
        11035 -> { // Seers Pub
            player.moveTo(x = 2773, z = 1760, 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.STAIRCASE_25935, option = "Climb-Up") {
    when(player.tile.regionId) {
        11035 -> { // Seers Pub
            player.moveTo(x = 2772, z = 1759, 1)
        }

        else -> player.message("Nothing interesting happens.")
    }
}


