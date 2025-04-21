import org.alter.plugins.content.magic.TeleportType
import org.alter.plugins.content.magic.teleport

on_obj_option(obj = Objs.LEVER_1816, option = "Pull") {
    when(player.tile.regionId) {
        12192 -> { //KBD
            player.teleport(type = TeleportType.MODERN, endTile = Tile (2271, 4680, 0))
        }

        else -> player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.LEVER_1817, option = "Pull") {
    when(player.tile.regionId) {
        9033 -> { //KBD
            player.teleport(type = TeleportType.MODERN, endTile = Tile (3067, 10254, 0))
        }

        else -> player.message("Nothing interesting happens.")
    }
}