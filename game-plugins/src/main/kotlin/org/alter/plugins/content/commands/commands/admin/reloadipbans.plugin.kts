package org.alter.plugins.content.commands

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.IpBanService

on_command("reloadipbans", Privilege.ADMIN_POWER) {
    // Herlaadt de ban-lijst vanaf schijf
    IpBanService.load()
    player.message("IP ban list reloaded (${IpBanService.count()} entries).")
}
