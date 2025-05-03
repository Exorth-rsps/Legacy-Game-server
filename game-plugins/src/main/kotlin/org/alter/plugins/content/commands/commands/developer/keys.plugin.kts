package org.alter.plugins.content.commands.commands.developer

import org.alter.game.model.priv.Privilege

on_command("keys", Privilege.DEV_POWER, description = "Gives all keys") {
    player.inventory.add(item = Items.KEY_1543, amount = 1)
    player.inventory.add(item = Items.KEY_1544, amount = 1)
    player.inventory.add(item = Items.KEY_1545, amount = 1)
    player.inventory.add(item = Items.GIANT_KEY, amount = 1)
    player.inventory.add(item = Items.MOSSY_KEY, amount = 1)
    player.inventory.add(item = Items.COLD_KEY, amount = 1)
    player.inventory.add(item = Items.ICY_KEY, amount = 1)
    player.inventory.add(item = Items.FROZEN_KEY, amount = 1)

}