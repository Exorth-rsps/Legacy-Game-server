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
on_obj_option(obj = Objs.STAIRCASE_16664, option = "Climb-Down") {
    when(player.tile.regionId) {
        10777 -> { // Yanille Dungeon
            player.moveTo(x = 3768, z = 1258, 0)
        }
        11032 -> { // Legends Dungeon
            player.moveTo(x = 3953, z = 1262, 0)
        }
        12084 -> { // Falador -> Dwarvenmine
            player.moveTo(x = 3058, z = 9776, 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.STAIRCASE_16665, option = "Climb-Up") {
    when(player.tile.regionId) {
        14867 -> { // Yanille Dungeon
            player.moveTo(x = 2695, z = 1629, 0)
        }
        15635 -> { // Legends Dungeon
            player.moveTo(x = 2758, z = 1590, 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.STAIRCASE_23969, option = "Climb-Up") {
    when(player.tile.regionId) {
        12184 -> { // Dwarven mines -> Falador
            player.moveTo(x = 3061, z = 3377, 0)
        }
        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.STAIRCASE_3415, option = "Climb-Down") {
    when(player.tile.regionId) {
        11035 -> { // Obor Dungeon
            player.moveTo(x = 4203, z = 1260, 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.STAIRCASE_3416, option = "Climb-Up") {
    when(player.tile.regionId) {
        16659 -> { // Obor Dungeon
            player.moveTo(x = 2775, z = 1748, 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}


