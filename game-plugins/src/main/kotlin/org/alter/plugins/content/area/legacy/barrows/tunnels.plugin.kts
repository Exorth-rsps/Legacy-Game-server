package org.alter.plugins.content.area.legacy.barrows

import org.alter.plugins.content.magic.TeleportType
import org.alter.plugins.content.magic.teleport

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */


on_obj_option(Objs.DOOR_20698, option = "Open") {
    player.teleport(type = TeleportType.ANCIENT, endTile = Tile (3565, 3316, 0))
}
on_obj_option(Objs.DOOR_20679, option = "Open") {
    player.teleport(type = TeleportType.ANCIENT, endTile = Tile (3565, 3316, 0))
}