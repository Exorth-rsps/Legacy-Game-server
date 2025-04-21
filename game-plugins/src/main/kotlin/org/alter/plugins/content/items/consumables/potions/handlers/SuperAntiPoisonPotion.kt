package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler
import org.alter.plugins.content.mechanics.poison.Poison


object SuperAntiPoisonPotion : PotionHandler {

    override val itemIds = listOf(
        Items.SUPERANTIPOISON4,
        Items.SUPERANTIPOISON3,
        Items.SUPERANTIPOISON2,
        Items.SUPERANTIPOISON1
    )

    private val replacementMap = mapOf(
        Items.SUPERANTIPOISON4 to Items.SUPERANTIPOISON3,
        Items.SUPERANTIPOISON3 to Items.SUPERANTIPOISON2,
        Items.SUPERANTIPOISON2 to Items.SUPERANTIPOISON1,
        Items.SUPERANTIPOISON1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {

        Poison.cureWithPotion(player, itemId)

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
