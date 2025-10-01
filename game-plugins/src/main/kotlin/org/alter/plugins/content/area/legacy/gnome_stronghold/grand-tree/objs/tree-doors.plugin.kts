package org.alter.plugins.content.area.legacy.gnome_stronghold.`grand-tree`.objs

import org.alter.game.model.entity.Player
import org.alter.game.model.Tile

fun Player.OpenTo(tile: Tile) {
    this.queue {
        animate(Animation.WARGUILD_PUSH_DOOR)
        lock()
        wait(2)
        moveTo(tile)
        unlock()
    }
}

on_obj_option(obj = Objs.TREE_DOOR, option = "Open") {
    when (player.tile) {
//        Tile(2465, 3491, 0) -> player.OpenTo(Tile(2465, 3493, 0))
//        Tile(2465, 3493, 0) -> player.OpenTo(Tile(2465, 3491, 0))
        else -> {
            player.message("The Grand Tree is Closed by King Narnode Shareen!")
            player.message("You must wait to his return to enter!")
        }
    }
}
on_obj_option(obj = Objs.TREE_DOOR_1968, option = "Open") {
    when (player.tile) {
//        Tile(2466, 3491, 0) -> player.OpenTo(Tile(2466, 3493, 0))
//        Tile(2466, 3493, 0) -> player.OpenTo(Tile(2466, 3491, 0))
        else -> {
            player.message("The Grand Tree is Closed by King Narnode Shareen!")
            player.message("You must wait to his return to enter!")
        }
    }
}

