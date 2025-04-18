package org.alter.plugins.content.area.legacy.dwarven_mine.obs

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */
on_obj_option(obj = Objs.TRAIN_CART_7029, option = "Ride") {
    if (player.getSkills().getCurrentLevel(Skills.AGILITY) < 70) {
        player.message("You need at least level 70 Agility to use this shortcut.")
        return@on_obj_option
    }
    player.moveTo(3060, 9766, player.tile.height)
}
on_obj_option(obj = Objs.CAVE_26654, option = "Enter") {
    if (player.getSkills().getCurrentLevel(Skills.AGILITY) < 70) {
        player.message("You need at least level 70 Agility to use this shortcut.")
        return@on_obj_option
    }
    player.moveTo(2995, 9837, player.tile.height)
}
on_obj_option(obj = Objs.MAGIC_DOOR_25115, option = "Open") {
    if (player.tile.x == 3049) {
        if (!player.inventory.contains(Items.MUDDY_KEY)) {
            player.message("You need a muddy key to open this door.")
            return@on_obj_option
        }
        player.moveTo(3051, 9840, player.tile.height)
    } else{
        player.moveTo(3049, 9840, player.tile.height)
    }
}
on_obj_option(obj = Objs.CREVICE_16543, option = "Squeeze-through") {
    if (player.getSkills().getCurrentLevel(Skills.AGILITY) < 40) {
        player.message("You need at least level 40 Agility to use this shortcut.")
        return@on_obj_option
    }
    if (player.tile.x == 3035) {
        player.moveTo(3028, 9806, player.tile.height)
    } else if (player.tile.x == 3028){
        player.moveTo(3035, 9806, player.tile.height)
    }
}
on_obj_option(obj = Objs.DOOR_30364, option = "Open") {
    if (player.tile.z == 9757) {
        if (player.getSkills().getCurrentLevel(Skills.MINING) < 60) {
            player.message("You need at least level 60 Mining to enter the Mining Guild.")
            return@on_obj_option
        }
        player.moveTo(3046, 9756, player.tile.height)
    } else{
        player.moveTo(3046, 9757, player.tile.height)
    }
}
on_obj_option(obj = Objs.CAVE_30374, option = "Enter") {
    if (player.getSkills().getCurrentLevel(Skills.MINING) < 70) {
        player.message("You need at least level 70 Mining to enter.")
        return@on_obj_option
    }
    player.moveTo(2838, 9387, 0)
}
on_obj_option(obj = Objs.DOOR_30366, option = "Open") {
    if (player.tile.z == 9730) {
        if (player.getSkills().getCurrentLevel(Skills.MINING) < 80) {
            player.message("You need at least level 80 Mining to enter.")
            return@on_obj_option
        }
        player.moveTo(3043, 9729, player.tile.height)
    } else{
        player.moveTo(3043, 9730, player.tile.height)
    }
}
on_obj_option(obj = Objs.DOOR_30365, option = "Open") {
    if (player.tile.z == 9733) {
        if (player.getSkills().getCurrentLevel(Skills.MINING) < 80) {
            player.message("You need at least level 80 Mining to enter.")
            return@on_obj_option
        }
        player.moveTo(3019, 9732, player.tile.height)
    } else{
        player.moveTo(3019, 9733, player.tile.height)
    }
}
