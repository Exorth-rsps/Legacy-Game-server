package gg.rsmod.plugins.content.areas.exorth.seers.dungeon

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */


on_obj_option(obj = Objs.ODD_LOOKING_WALL_26115, option = "Open", lineOfSightDistance = 1) {
    if (player.tile.x == 2772 && player.tile.z == 1748) {
        if (!player.inventory.contains(Items.GIANT_KEY)) {
            player.message("You need a key to open this wall.")
            return@on_obj_option
        }
        player.moveTo(2773, 1748, player.tile.height)
    } else if (player.tile.x == 2773 && player.tile.z == 1748) {
        player.moveTo(2772, 1748, player.tile.height)
    } else {
        player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.ODD_LOOKING_WALL_26114, option = "Open", lineOfSightDistance = 1) {
    if (player.tile.x == 2772 && player.tile.z == 1747) {
        if (!player.inventory.contains(Items.GIANT_KEY)) {
            player.message("You need a key to open this wall.")
            return@on_obj_option
        }
        player.moveTo(2773, 1747, player.tile.height)
    } else if (player.tile.x == 2773 && player.tile.z == 1747) {
        player.moveTo(2772, 1747, player.tile.height)
    } else {
        player.message("Nothing interesting happens.")
    }
}

on_obj_option(obj = Objs.GATE_29486, option = "Open", lineOfSightDistance = 1) {
    if (!player.inventory.contains(Items.GIANT_KEY)) {
        player.message("You need a key to open this gate.")
        return@on_obj_option
    }

    player.queue {
        player.inventory.remove(Items.GIANT_KEY, 1)
        val x = 4178
        val z = 1239
        player.moveTo(tile = Tile(x = x, z = z))
    }
}
on_obj_option(obj = Objs.GATE_29487, option = "Open", lineOfSightDistance = 1) {
    if (!player.inventory.contains(Items.GIANT_KEY)) {
        player.message("You need a key to open this gate.")
        return@on_obj_option
    }

    player.queue {
        player.inventory.remove(Items.GIANT_KEY, 1)
        val x = 4179
        val z = 1239
        player.moveTo(tile = Tile(x = x, z = z))
    }
}
on_obj_option(obj = Objs.GATE_29489, option = "Open", lineOfSightDistance = 1) {
    player.queue {
        val x = 4182
        val z = 1257
        player.moveTo(tile = Tile(x = x, z = z))
    }
}
on_obj_option(obj = Objs.GATE_29488, option = "Open", lineOfSightDistance = 1) {

    player.queue {
        val x = 4182
        val z = 1256
        player.moveTo(tile = Tile(x = x, z = z))
    }
}
//TO-DO: Facing issues
on_obj_option(obj = Objs.ROCKS_29491, option = 1) {
    val obj = player.getInteractingGameObj()
    val isNorth = player.tile.z > obj.tile.z
    val offsetZ = if (isNorth) -1 else 1
    val StartTile = Tile(obj.tile.x, obj.tile.z)
    val EndTile = Tile(obj.tile.x, obj.tile.z + 3 * offsetZ)

    player.lockingQueue {
        if (player.tile != StartTile) {
            player.faceDirection(Direction.NORTH)
            val distance = player.tile.getDistance(StartTile)
            player.walkTo(StartTile)
            wait(distance + 2)
        } else {
            player.faceDirection(Direction.NORTH)
            wait(2)
        }

        val faceTile = Tile(obj.tile.x, obj.tile.z + (if (isNorth) -1 else 1))
        player.faceTile(faceTile)
        wait(1)

        // Loop for forced movement
        for (i in 1..2) {
            player.animate(if (i == 1) 740 else 737)

            val nextTile = Tile(obj.tile.x, obj.tile.z + i * offsetZ)

            player.queue {
                player.faceDirection(Direction.NORTH)
                val move = ForcedMovement.of(
                    player.tile,
                    nextTile,
                    clientDuration1 = if (i == 1) 25 else 25,
                    clientDuration2 = 60,
                    directionAngle = if (isNorth) Direction.NORTH.ordinal else Direction.SOUTH.ordinal,
                    lockState = LockState.NONE
                )
                player.faceDirection(Direction.NORTH)
                player.forceMove(this, move)
            }
            wait(1)
        }
        player.animate(737)
        waitTile(EndTile)
        player.animate(738)
    }
}





