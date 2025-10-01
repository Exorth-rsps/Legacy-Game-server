package org.alter.plugins.content.area.legacy.gnome_stronghold.objs

import org.alter.game.model.entity.Player
import org.alter.game.model.Tile

fun Player.climbTo(tile: Tile) {
    this.queue {
        animate(Animation.CLIMB_CAVE)
        lock()
        wait(2)
        moveTo(tile)
        unlock()
    }
}

on_obj_option(obj = Objs.CAVE_26709, option = "Enter", lineOfSightDistance = 2) {
    when (player.tile) {
        Tile(2430, 3424, 0) -> player.climbTo(Tile(2429, 9825, 0)) //Slayer Cave
        else -> player.message("Nothing interesting happens...")
    }
}
on_obj_option(obj = Objs.CAVE_ENTRANCE_17209, option = "Enter", lineOfSightDistance = 2) {
    when (player.tile) {
        Tile(2402, 3419, 0) -> player.climbTo(Tile(2408, 9812, 0)) //Brimstail
        else -> player.message("Nothing interesting happens...")
    }
}
