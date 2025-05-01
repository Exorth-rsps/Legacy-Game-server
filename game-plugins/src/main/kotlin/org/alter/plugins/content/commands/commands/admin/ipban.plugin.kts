package org.alter.plugins.content.commands

import org.alter.game.message.impl.LogoutFullMessage
import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.IpBanService
import java.net.InetSocketAddress

on_command("ipban", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    if (args.isEmpty()) {
        player.message("Usage: ::ipban <player_name>")
        return@on_command
    }
    val targetName = args.joinToString(" ")
    val target = world.getPlayerForName(targetName)

    if (target != null) {
        // 1) ban account
        target.privilege = world.privileges.get(-1)!!

        // 2) fetch IP from our map
        val ip = IpBanService.getIpForUser(target.username)
        if (ip == null) {
            player.message("Kon IP van '$targetName' niet vinden.")
            return@on_command
        }

        // 3) ban IP
        IpBanService.add(ip)

        // 4) kick speler
        target.requestLogout()
        target.channelClose()

        // 5) (optioneel) herlaad file zodat disk synchroon loopt
        IpBanService.load()

        player.message("Player '$targetName' and IP '$ip' are now banned and kicked.")
    } else {
        player.message("Player '$targetName' not found.")
    }
}

