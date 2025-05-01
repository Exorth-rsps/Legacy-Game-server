package org.alter.plugins.content.commands

import org.alter.game.message.impl.LogoutFullMessage
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

on_command("ban", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    if (args.isEmpty()) {
    tryWithUsage(player, args, "ban <player_name>")
        return@on_command
    }

        val targetName = args.joinToString(" ")
        val target = world.getPlayerForName(targetName)

        if (target != null) {
            target.privilege = world.privileges.get(-1)!!

            target.requestLogout()
            target.write(LogoutFullMessage())
            target.channelClose()

            player.message("Player '$targetName' is banned and kicked.")
        } else {
            player.message("Player '$targetName' not found.")
        }
    }
}
