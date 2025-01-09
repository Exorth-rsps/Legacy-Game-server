package gg.rsmod.plugins.content.areas.exorth.yanille.dungeon

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */

val doors = listOf(
    Triple(Items.KEY_1543, Tile(x = 3765, z = 1257), "Door"),
    Triple(Items.KEY_1544, Tile(x = 3754, z = 1255), "Door"),
    Triple(Items.KEY_1545, Tile(x = 3731, z = 1235), "Door")
)

on_obj_option(obj = Objs.DOOR_11728, option = "Open") {
    val doorData = doors.find { (_, tile, _) ->
        player.tile == tile || player.tile == tile.transform(1, 0)
    }

    if (doorData == null) {
        player.message("You need to be near the door to open it.")
        return@on_obj_option
    }

    val (keyId, doorTile, doorName) = doorData

    if (!player.inventory.contains(keyId)) {
        player.message("This $doorName is locked.")
        return@on_obj_option
    }

    handleDoor(player, Objs.DOOR_11728, doorTile)
}

fun handleDoor(player: Player, doorId: Int, doorTile: Tile) {
    val closedDoor = DynamicObject(id = doorId, type = 0, rot = 2, tile = doorTile)
    val doorRot = 1 // Deuren zijn consistent georiënteerd
    val door = DynamicObject(id = doorId, type = 0, rot = doorRot, tile = doorTile)

    player.lock = LockState.DELAY_ACTIONS
    world.remove(closedDoor)
    player.playSound(Sound.DOOR_OPEN)
    world.spawn(door)

    player.queue {
        // Verplaats speler naar de andere kant van de deur
        val newTile = if (player.tile.x > doorTile.x) {
            doorTile // Beweeg naar links
        } else {
            doorTile.transform(1, 0) // Beweeg naar rechts
        }
        player.walkTo(tile = newTile, detectCollision = false)
        wait(3)
        world.remove(door)
        player.lock = LockState.NONE
        world.spawn(closedDoor)
        player.playSound(Sound.DOOR_CLOSE)
    }
}

on_obj_option(obj = Objs.GATE_2155, option = "Open") {
    when(player.tile.regionId) {
        14867 -> {
            player.message("The gates are locked by a magical force!")
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.GATE_2154, option = "Open") {
    when(player.tile.regionId) {
        14867 -> {
            player.message("The gates are locked by a magical force!")
        }

        else -> player.message("Nothing interesting happens.")
    }
}