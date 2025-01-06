package gg.rsmod.plugins.content.areas.exorth.yanille.dungeon
/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */

on_obj_option(obj = Objs.DOOR_11728, option = "open") {
    // Haal het object op basis van de ID van het object dat wordt gebruikt
    val objInstance = world.getObject(player.tile, type = 0) // Je moet hier player.tile vervangen door de tegel waar je object staat.

    if (objInstance == null) {
        player.message("The door does not exist.")
        return@on_obj_option
    }

    val objTile = objInstance.tile // Haal de tegel op van het object

    when (objTile) { // Controleer de tegel van het object
        Tile(x = 2601, z = 9482, height = 0) -> {
            if (!player.inventory.contains(Items.KEY_1543)) {
                player.message("The door is locked.")
                return@on_obj_option
            }
            handleDoor(player, objTile)
        }
        Tile(x = 2603, z = 9482, height = 0) -> {
            if (!player.inventory.contains(Items.KEY_1544)) {
                player.message("The door is locked.")
                return@on_obj_option
            }
            handleDoor(player, objTile)
        }
        Tile(x = 2605, z = 9482, height = 0) -> {
            if (!player.inventory.contains(Items.KEY_1545)) {
                player.message("The door is locked.")
                return@on_obj_option
            }
            handleDoor(player, objTile)
        }
        else -> {
            player.message("This door cannot be opened.")
        }
    }
}



fun handleDoor(player: Player, doorTile: Tile) {
    val closedDoor = DynamicObject(id = 11728, type = 0, rot = 3, tile = doorTile)
    val door = DynamicObject(
        id = 11728,
        type = 0,
        rot = if (player.tile.z == doorTile.z - 1) 2 else 2,
        tile = doorTile
    )
    player.lock = LockState.DELAY_ACTIONS
    world.remove(closedDoor)
    world.spawn(door)

    player.queue {
        val x = doorTile.x
        val z = if (player.tile.z == doorTile.z - 1) doorTile.z else doorTile.z - 1
        player.walkTo(tile = Tile(x = x, z = z), detectCollision = false)
        wait(3)
        world.remove(door)
        player.lock = LockState.NONE
        world.spawn(closedDoor)
    }
}
