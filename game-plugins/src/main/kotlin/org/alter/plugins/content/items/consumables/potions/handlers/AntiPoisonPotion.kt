// src/main/kotlin/org/alter/plugins/content/items/consumables/potions/handlers/AntiPoisonPotion.kt
package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler
import org.alter.plugins.content.mechanics.poison.Poison

/**
 * Eén handler voor ANTIPOISON4 → 3 → 2 → 1 → VIAL
 */
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
        // 1) Cure poison
        Poison.cureWithPotion(player, itemId)
        // 2) Animatie & graphic
        player.animate(829)
        // 3) Inventory: huidige dose eruit, replacement erin
        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)
    }
}
