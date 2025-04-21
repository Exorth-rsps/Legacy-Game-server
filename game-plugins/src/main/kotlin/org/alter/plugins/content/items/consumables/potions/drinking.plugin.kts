package org.alter.plugins.content.items.consumables.potions

import org.alter.plugins.content.items.consumables.potions.Potions

Potions.handlers.forEach { handler ->
    handler.itemIds.forEach { itemId ->
        on_item_option(item = itemId, option = "drink") {
            // roept jouw PotionHandler aan
            handler.onDrink(player, itemId)

            // optioneel feedback (zonder item‑object)
            player.message("You drink a dose of your potion.")
        }
    }
}
