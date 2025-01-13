package gg.rsmod.plugins.content.areas.exorth.mainland.dungeons.bryophya

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */
on_obj_option(obj = Objs.GATE_32534, option = "open", lineOfSightDistance = 0) {
    val isNorthSide = player.tile.z >= 1237

    if (isNorthSide && !player.inventory.contains(Items.MOSSY_KEY)) {
        player.message("The door is locked.")
        return@on_obj_option
    }

    handleDoor(player, isNorthSide)
}

fun handleDoor(player: Player, isNorthSide: Boolean) {
    val closedDoor = DynamicObject(id = 32534, type = 0, rot = 1, tile = Tile(x = 4041, z = 1236))
    val door =
        DynamicObject(
            id = 32534,
            type = 0,
            rot =
                if (isNorthSide) {
                    2
                } else {
                    0
                },
            tile = Tile(x = 4041, z = 1236),
        )
    player.lock = LockState.DELAY_ACTIONS
    world.remove(closedDoor)
    player.playSound(Sound.DOOR_OPEN)
    world.spawn(door)

    player.queue {
        val x = 4041
        val z = if (isNorthSide) 1236 else 1237
        player.walkTo(tile = Tile(x = x, z = z), detectCollision = false)
        wait(3)
        world.remove(door)
        player.lock = LockState.NONE
        player.inventory.remove(Items.MOSSY_KEY, 1)
        world.spawn(closedDoor)
        player.playSound(Sound.DOOR_CLOSE)
    }
}
on_obj_option(obj = Objs.LOGS_32536, option = "Take-axe", lineOfSightDistance = 1) {
    val obj = player.getInteractingGameObj()
    player.queue {
            if(player.inventory.isFull) {
                player.message("You don't have room for this.")
                return@queue
            } else {
                player.inventory.add(Items.BRONZE_AXE)
            }

    }

}