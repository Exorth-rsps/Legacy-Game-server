package gg.rsmod.plugins.content.areas.yanille
/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */

// Doors
//To-Do:    Close doors after opening, the closing wont work atm.
//          Also need to move player when opening.
//          When players now try to close, this plugin trows an error.
//          Nothing is failing after the error.
val DOORS = intArrayOf(Objs.MAGIC_GUILD_DOOR, Objs.MAGIC_GUILD_DOOR_1733)

DOORS.forEach { obj ->
    on_obj_option(obj, "Open", lineOfSightDistance = 2) {
        if (obj == 1732) {
            player.lock()
            world.queue {
                world.openDoor(world.getObject(Tile(2686, 1638), 0)!!, Objs.MAGIC_GUILD_DOOR, invertRot = true)
                world.openDoor(world.getObject(Tile(2686, 1638), 0)!!, Objs.MAGIC_GUILD_DOOR_1733)
                world.openDoor(world.getObject(Tile(2673, 1637), 0)!!, Objs.MAGIC_GUILD_DOOR, invertRot = true)
                world.openDoor(world.getObject(Tile(2673, 1637), 0)!!, Objs.MAGIC_GUILD_DOOR_1733)

            }
            player.unlock()
        }
        if (obj == 1733) {
            player.lock()
            world.queue {
                world.openDoor(world.getObject(Tile(2686, 1637), 0)!!, Objs.MAGIC_GUILD_DOOR, invertRot = true)
                world.openDoor(world.getObject(Tile(2686, 1638), 0)!!, Objs.MAGIC_GUILD_DOOR_1733)
                world.openDoor(world.getObject(Tile(2673, 1638), 0)!!, Objs.MAGIC_GUILD_DOOR, invertRot = true)
                world.openDoor(world.getObject(Tile(2673, 1637), 0)!!, Objs.MAGIC_GUILD_DOOR_1733)

            }
            player.unlock()
        }
    }
}

//Portals
//East Portal
on_obj_option(obj = Objs.MAGIC_PORTAL, option = "Enter") {
    //player.moveTo(3109, 3163, 0)
    player.message("Nothing interesting happens.")
}
//South Portal
on_obj_option(obj = Objs.MAGIC_PORTAL_2157, option = "Enter") {
    //player.moveTo(2908, 3336, 0)
    player.message("Nothing interesting happens.")
}
//South Portal
on_obj_option(obj = Objs.MAGIC_PORTAL_2158, option = "Enter") {
    //player.moveTo(2702, 3405, 0)
    player.message("Nothing interesting happens.")
}



