package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.priv.Privilege

on_command("reset", Privilege.ADMIN_POWER, description = "Reset all skills to lowest level") {
    for (i in 0 until player.getSkills().maxSkills) {
        player.getSkills().setBaseLevel(i, if (i == Skills.HITPOINTS) 10 else 1)
    }
    player.calculateAndSetCombatLevel()
}