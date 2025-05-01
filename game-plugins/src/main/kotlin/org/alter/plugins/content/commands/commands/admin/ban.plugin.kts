package org.alter.plugins.content.commands

import org.alter.game.message.impl.LogoutFullMessage
import org.alter.game.model.priv.Privilege

on_command("ban", Privilege.MOD_POWER) {
    val args = player.getCommandArgs()
    if (args.isEmpty()) {
        player.message("Usage: ::ban <player_name>")
        return@on_command
    }

    // Steek alle woorden in één string, zodat "Jan Jansen" werkt
    val targetName = args.joinToString(" ")
    val target = world.getPlayerForName(targetName)

    if (target != null) {
        // Stel de ban-privilege in (id -1)
        target.privilege = world.privileges.get(-1)!!

        // Log de target uit en sluit de channel
        target.requestLogout()
        target.write(LogoutFullMessage())
        target.channelClose()

        player.message("Player '$targetName' is banned and kicked.")
    } else {
        player.message("Player '$targetName' not found.")
    }
}
