package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

/**
 *   @Author Cl0ud
 */
on_command("setrights", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "setrights <player_name> <privilege_id>") { values ->
        val playerName = values[0]
        val privilege = values[1].toInt()

        val targetPlayer = world.getPlayerForName(playerName)
        targetPlayer?.privilege = world.privileges.get(privilege)!!
    }
}
