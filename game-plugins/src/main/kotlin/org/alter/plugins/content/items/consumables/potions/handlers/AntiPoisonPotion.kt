package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler
import org.alter.plugins.content.mechanics.poison.Poison


object AntiPoisonPotion : PotionHandler {

    override val itemIds = listOf(
        Items.ANTIPOISON4,
        Items.ANTIPOISON3,
        Items.ANTIPOISON2,
        Items.ANTIPOISON1
    )

    private val replacementMap = mapOf(
        Items.ANTIPOISON4 to Items.ANTIPOISON3,
        Items.ANTIPOISON3 to Items.ANTIPOISON2,
        Items.ANTIPOISON2 to Items.ANTIPOISON1,
        Items.ANTIPOISON1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        Poison.cureWithPotion(player, itemId)
        player.animate(829)
        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)
    }
}
