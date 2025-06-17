package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege
import org.alter.api.cfg.Npcs
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage
import org.alter.plugins.content.mechanics.random_events.spawnRandomEvent

on_command("randomevent", Privilege.DEV_POWER, description = "Spawn a random event") {
    val args = player.getCommandArgs()
    if (args.isEmpty()) {
        spawnRandomEvent(player)
    } else {
        val name = args.joinToString(" ").lowercase()
        val npcId = when (name) {
            "genie" -> Npcs.GENIE
            "gravedigger", "leo" -> Npcs.LEO
            "freaky forester", "forester" -> Npcs.FREAKY_FORESTER_6748
            "sandwich lady", "sandwich" -> Npcs.SANDWICH_LADY
            else -> -1
        }
        if (npcId == -1) {
            player.message("Unknown event: $name")
        } else {
            spawnRandomEvent(player, npcId)
        }
    }
}
