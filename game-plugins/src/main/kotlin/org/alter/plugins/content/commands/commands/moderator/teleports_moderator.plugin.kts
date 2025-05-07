package org.alter.plugins.content.commands.commands.moderator

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.magic.TeleportType
import org.alter.plugins.content.magic.canTeleport
import org.alter.plugins.content.magic.teleport


on_command("staffzone", Privilege.MOD_POWER, description = "Teleports you to the staff map") {
    player.teleport(type = TeleportType.ARCEUUS, endTile = Tile (2694, 1643, 0))
}
on_command("home", Privilege.MOD_POWER, description = "Teleports you home") {
    val home = world.gameContext.home
    player.moveTo(home)
}

