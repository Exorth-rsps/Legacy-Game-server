package org.alter.plugins.content.area.legacy.gnome_stronghold.`grand-tree`.objs

import org.alter.game.model.entity.Player
import org.alter.game.model.Tile

fun Player.climbTo(tile: Tile) {
    this.queue {
        animate(828)
        lock()
        wait(2)
        moveTo(tile)
        unlock()
    }
}

on_obj_option(obj = Objs.TRAPDOOR_2446, option = "Open", lineOfSightDistance = 2) {
    when (player.tile) {
        Tile(2464, 3497, 0) -> player.climbTo(Tile(2464, 9897, 0)) //Grand Tree Tunnels
        else -> player.message("Nothing interesting happens...")
    }
}
