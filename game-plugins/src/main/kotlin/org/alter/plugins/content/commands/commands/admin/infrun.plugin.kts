package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.bits.*
import org.alter.game.model.priv.Privilege

on_command("infrun", Privilege.ADMIN_POWER, description = "Infinite run energy") {
    player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.RUN)
    player.message("Infinite run: ${if (!player.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.RUN)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}