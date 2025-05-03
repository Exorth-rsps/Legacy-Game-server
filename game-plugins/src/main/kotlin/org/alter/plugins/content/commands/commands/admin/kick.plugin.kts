package org.alter.plugins.content.commands

import org.alter.game.message.impl.LogoutFullMessage
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

/**
 *  Kick command: dwingt een speler uit te loggen zonder verdere wijzigingen.
 */
on_command("kick", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "kick <player_name>") { values ->
        val targetName = values[0]
        val target = world.getPlayerForName(targetName)

        if (target != null) {
            // Forceer logout
            target.requestLogout()
            target.write(LogoutFullMessage())
            target.channelClose()

            player.message("Player '$targetName' is kicked.")
        } else {
            player.message("Player '$targetName' not found.")
        }
    }
}