package org.alter.plugins.content.commands.commands.moderator

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

on_command("teletome", Privilege.MOD_POWER, description = "Teleport a player to you") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::telehere PlayerName</col>") { values ->
        val targetPlayerName = values[0]
        val targetPlayer = world.getPlayerForName(targetPlayerName)

        if (targetPlayer != null) {
            // Teleport target player to your location
            targetPlayer.moveTo(player.tile)
        } else {
            player.message("<col=801700>Player not found!</col>")
        }
    }
}


