package org.alter.plugins.content.area.legacy.asgarnian_ice_dungeon.objs

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */
on_obj_option(obj = Objs.LADDER, option = "climb-down", lineOfSightDistance = 1) {
    when (player.tile.regionId) {
        11829 -> {
                if (!player.inventory.contains(Items.COLD_KEY)) {
                    player.message("You need a cold key to use this ladder.")
                    return@on_obj_option
                }
                player.moveTo(3007, 9550, player.tile.height)
            }
    }
}
on_obj_option(obj = Objs.TUNNEL_42506, option = "Enter") {
    if (!player.inventory.contains(Items.ICY_KEY) || player.getSkills().getCurrentLevel(Skills.AGILITY) < 50) {
        player.message("You need an icy key and at least level 50 Agility to use this tunnel.")
        return@on_obj_option
    }
    player.moveTo(3035, 9557, player.tile.height)
}
on_obj_option(obj = Objs.TUNNEL_42507, option = "Enter") {
    if (!player.inventory.contains(Items.ICY_KEY) || player.getSkills().getCurrentLevel(Skills.AGILITY) < 50) {
        player.message("You need an icy key and at least level 50 Agility to use this tunnel.")
        return@on_obj_option
    }
    player.moveTo(3026, 9572, player.tile.height)
}
on_obj_option(obj = Objs.ICY_CAVERN_10596, option = "Enter") {
    if (!player.inventory.contains(Items.ICY_KEY)) {
        player.message("You need an icy key to use this tunnel.")
        return@on_obj_option
    }
    player.moveTo(3056, 9555, player.tile.height)
}
on_obj_option(obj = Objs.ICY_CAVERN, option = "Enter") {
    if (!player.inventory.contains(Items.ICY_KEY)) {
        player.message("You need an icy key to use this tunnel.")
        return@on_obj_option
    }
    player.moveTo(3056, 9562, player.tile.height)
}

on_obj_option(obj = Objs.STEPS, option = "Climb") {
    if (player.tile.x == 3060 && player.tile.z == 9555) {
        if (!player.inventory.contains(Items.FROZEN_KEY)) {
            player.message("You need a frozen key to open this wall.")
            return@on_obj_option
        }
        player.moveTo(3060, 9557, 0)
    } else if (player.tile.x == 3060 && player.tile.z == 9557) {
        player.moveTo(3060, 9555, 0)
    } else {
        player.message("Nothing interesting happens.")
    }
}