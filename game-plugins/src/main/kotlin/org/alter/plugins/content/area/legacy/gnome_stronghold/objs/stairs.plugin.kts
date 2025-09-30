package org.alter.plugins.content.area.legacy.gnome_stronghold.objs

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

on_obj_option(obj = Objs.STAIRCASE_16675, option = "climb-Up", lineOfSightDistance = 2) {
    when (player.tile) {
        Tile(2443, 3414, 0) -> player.climbTo(Tile(2445, 3416, 1)) //Bank
        Tile(2444, 3434, 0) -> player.climbTo(Tile(2445, 3433, 1)) //Bank
        Tile(2455, 3416, 0) -> player.climbTo(Tile(2457, 3417, 1))
        Tile(2460, 3416, 0) -> player.climbTo(Tile(2460, 3417, 1))
        Tile(2478, 3408, 0) -> player.climbTo(Tile(2460, 3407, 1))
        Tile(2486, 3407, 0) -> player.climbTo(Tile(2489, 3409, 1)) //Spinningwheel 1
        Tile(2484, 3402, 0) -> player.climbTo(Tile(2485, 3401, 1)) //Spinningwheel 2
        Tile(2474, 3400, 0) -> player.climbTo(Tile(2475, 3399, 1)) //Spinningwheel 3
        Tile(2439, 3404, 0) -> player.climbTo(Tile(2440, 3403, 1))
        Tile(2417, 3417, 0) -> player.climbTo(Tile(2418, 3416, 1))
        else -> player.message("Nothing interesting happens...")
    }
}

on_obj_option(obj = Objs.STAIRCASE_16677, option = "climb-Down") {
    when (player.tile) {
        Tile(2445, 3416, 1) -> player.climbTo(Tile(2446, 3415, 0)) //Bank
        Tile(2445, 3433, 1) -> player.climbTo(Tile(2444, 3434, 0)) //Bank
        Tile(2457, 3417, 1) -> player.climbTo(Tile(2455, 3416, 0))
        Tile(2460, 3417, 1) -> player.climbTo(Tile(2460, 3416, 0))
        Tile(2479, 3407, 1) -> player.climbTo(Tile(2478, 3404, 0))
        Tile(2489, 3409, 1) -> player.climbTo(Tile(2486, 3407, 0)) //Spinningwheel 1
        Tile(2485, 3401, 1) -> player.climbTo(Tile(2484, 3402, 0)) //Spinningwheel 2
        Tile(2475, 3399, 1) -> player.climbTo(Tile(2474, 3400, 0)) //Spinningwheel 3
        Tile(2474, 3400, 1) -> player.climbTo(Tile(2439, 3404, 0))
        Tile(2418, 3416, 1) -> player.climbTo(Tile(2417, 3417, 0))
        else -> player.message("Nothing interesting happens...")
    }
}
