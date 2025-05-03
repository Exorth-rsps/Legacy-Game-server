package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.priv.Privilege

on_command("invisible", Privilege.ADMIN_POWER, description = "Become a ghost?") {
    player.invisible = !player.invisible
    player.message("Invisible: ${if (!player.invisible) "<col=801700>false</col>" else "<col=178000>true</col>"}")
}