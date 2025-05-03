package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege

on_command("food", Privilege.DEV_POWER, description = "Fills your inventory with food") {
    player.inventory.add(item = Items.MANTA_RAY, amount = player.inventory.freeSlotCount)
}