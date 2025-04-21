package org.alter.plugins.content.items.consumables.potions

import org.alter.plugins.content.items.consumables.potions.handlers.AntiPoisonPotion
import org.alter.game.model.entity.Player



/**
 * Basis voor elke „drinkbare“ potion: per itemId + replacementId
 * én een eigen onDrink‑logica.
 */
interface PotionHandler {
    /** Alle item‑IDs die deze handler afvangt (4‑3‑2‑1 doses). */
    val itemIds: List<Int>
    /**
     * Called when player drinks één van de itemIds.
     * @param player de drinker
     * @param itemId welk specifiek item (4,3,2 of 1 dose)
     */
    fun onDrink(player: Player, itemId: Int)
}

/**
 * Verzameling van alle handlers – wordt gebruikt in de plugin om
 * automatisch voor elke handler een `on_item_option(...)` te registreren.
 */
object Potions {
    val handlers: List<PotionHandler> = listOf(
        AntiPoisonPotion
        // + andere handlers
    )
}
