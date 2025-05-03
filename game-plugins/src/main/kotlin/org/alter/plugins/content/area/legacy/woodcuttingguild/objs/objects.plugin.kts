package org.alter.plugins.content.area.legacy.woodcuttingguild.objs

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */
on_obj_option(obj = Objs.ROPE_LADDER_28857, option = "Climb-up") {
    when(player.tile.z) {
        3483 -> {
            player.moveTo(x = 1574, z = 3483, height = 1)
        }
        3493 -> {
            player.moveTo(x = 1574, z = 3493, height = 1)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.ROPE_LADDER_28858, option = "Climb-down") {
    when(player.tile.z) {
        3483 -> {
            player.moveTo(x = 1575, z = 3483, height = 0)
        }
        3493 -> {
            player.moveTo(x = 1575, z = 3493, height = 0)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.CARVED_REDWOOD_29681, option = "Enter") {
    when(player.tile.z) {
        3486 -> {
            player.moveTo(x = 1571, z = 3486, height = 2)
        }
        3489 -> {
            player.moveTo(x = 1571, z = 3489, height = 2)
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.CARVED_REDWOOD_29682, option = "Enter") {
    when(player.tile.z) {
        3486 -> {
            player.moveTo(x = 1571, z = 3486, height = 1)
        }
        3489 -> {
            player.moveTo(x = 1571, z = 3489, height = 1)
        }

        else -> player.message("Nothing interesting happens.")
    }
}