package org.alter.plugins.content.commands.commands.moderator

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage
import org.alter.plugins.content.magic.TeleportType
import org.alter.plugins.content.magic.teleport
import org.alter.game.model.Tile

on_command("mute", Privilege.MOD_POWER, description = "Teleport a player to you and ban them") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Proper usage: <col=801700>::mute PlayerName</col>") { values ->
        val targetName = values[0]
        val target = world.getPlayerForName(targetName)

        if (target != null) {
            // Teleport target player naar jouw locatie (of een vaste locatie)
            target.teleport(type = TeleportType.CABBAGE, endTile = Tile(2542, 5516, 0))

            // Stel de ban-privilege in (id = -1)
            target.privilege = world.privileges.get(-1)!!

            // Feedback aan de moderator
            player.message("<col=00FF00>Player '$targetName' is now banned and teleported.</col>")
        } else {
            player.message("<col=FF0000>Player '$targetName' not found.</col>")
        }
    }
}
