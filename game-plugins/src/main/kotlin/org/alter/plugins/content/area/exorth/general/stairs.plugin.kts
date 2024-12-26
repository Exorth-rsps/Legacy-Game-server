package gg.rsmod.plugins.content.areas.yanille
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

