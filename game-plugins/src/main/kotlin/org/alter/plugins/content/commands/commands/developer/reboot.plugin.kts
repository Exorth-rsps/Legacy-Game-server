package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege
import org.alter.game.model.timer.FORCE_DISCONNECTION_TIMER
import org.alter.plugins.content.commands.Commands_plugin.Command.tryWithUsage
import kotlin.system.exitProcess

on_command("reboot", Privilege.DEV_POWER, description = "Restart Server") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::reboot 500</col>") { values ->
        val cycles = values[0].toInt()
        world.queue {
            world.rebootTimer = cycles
            world.sendRebootTimer(cycles)
            wait(cycles)
            world.players.forEach { player -> player.timers[FORCE_DISCONNECTION_TIMER] = 0 }
            wait(5)
            exitProcess(0)
        }
    }
}