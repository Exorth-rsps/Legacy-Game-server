package org.alter.plugins.content.commands.commands.moderator

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage

on_command("teleto", Privilege.MOD_POWER, description = "Teleport to another player") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::teleto PlayerName</col>") { values ->
        val targetPlayerName = values[0]
        val targetPlayer = world.getPlayerForName(targetPlayerName)

        if (targetPlayer != null) {
            // Teleport to target player’s location
            player.moveTo(targetPlayer.tile)
        } else {
            player.message("<col=801700>Player not found!</col>")
        }
    }
}

