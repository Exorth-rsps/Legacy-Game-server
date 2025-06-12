package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.magic.TeleportType
import org.alter.plugins.content.magic.canTeleport
import org.alter.plugins.content.magic.teleport


on_command("westfally", Privilege.ADMIN_POWER, description = "Teleports you to west fally") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (3033, 3356, 0))
}
on_command("icekeydungeon", Privilege.ADMIN_POWER, description = "Teleports you to the ice key dungeon") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (3048, 9582, 0))
}
on_command("dwarvenmines", Privilege.ADMIN_POWER, description = "Teleports you to the dwarvenmines") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (3021, 9812, 0))
}
on_command("entrana", Privilege.ADMIN_POWER, description = "Teleports you to entrana") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (2826, 3343, 0))
}
on_command("legendsguild", Privilege.ADMIN_POWER, description = "Teleports you to the legendsguild") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (2728, 3375, 0))
}
on_command("shilo", Privilege.ADMIN_POWER, description = "Teleports you to shilo") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (2856, 2960, 0))
}
on_command("edge", Privilege.ADMIN_POWER, description = "Teleports you to edge") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (3087, 3494, 0))
}
on_command("gnome", Privilege.ADMIN_POWER, description = "Teleports you to the gnome agility") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (2480, 3436, 0))
}
on_command("varrock", Privilege.ADMIN_POWER, description = "Teleports you to varrock") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (3217, 3429, 0))
}
on_command("pk", Privilege.ADMIN_POWER, description = "Teleports you to the pkzone") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (3420, 4063, 0))
}
on_command("mortton", Privilege.ADMIN_POWER, description = "Teleports you to morrton") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (3520, 3285, 0))
}
on_command("barrows", Privilege.ADMIN_POWER, description = "Teleports you to the barrows") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (3565, 3290, 0))
}