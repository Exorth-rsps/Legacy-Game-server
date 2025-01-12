package gg.rsmod.plugins.content.areas.exorth.yanille.dungeon

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */

on_obj_option(obj = Objs.EXIT_18354, option = "Climb-up") { //exit
    player.moveTo(3109, 3163, 0)
}
on_obj_option(obj = Objs.OLD_RUIN_ENTRANCE, option = "Climb-down") { //entrance
    player.moveTo(3464, 1235, 0)
}
on_obj_option(obj = Objs.WALL_18359, option = "Push") {
    player.message("Nothing interesting happens.")
}