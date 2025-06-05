package org.alter.plugins.content.area.legacy.mortton.objs

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */
on_obj_option(obj = Objs.STAIRS_4919, option = "Walk-Down") {
    val locations = listOf(
        Tile(x = 3361, z = 10274, height = 0),
        Tile(x = 3381, z = 10286, height = 0),
        Tile(x = 3359, z = 10246, height = 0),
        Tile(x = 3338, z = 10286, height = 0)
    )
    player.moveTo(locations.random())
}
on_obj_option(obj = Objs.EXIT_47149, option = "Climb-Up") {
    player.moveTo(3454, 3242, 0)
}
on_obj_option(obj = Objs.CAVE_47148, option = "Exit") {
    player.moveTo(3454, 3242, 0)
}
on_obj_option(obj = Objs.CAVE_47147, option = "Exit") {
    player.moveTo(3454, 3242, 0)
}

