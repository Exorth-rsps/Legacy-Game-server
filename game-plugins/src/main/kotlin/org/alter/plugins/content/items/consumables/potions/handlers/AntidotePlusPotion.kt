
package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler
import org.alter.plugins.content.mechanics.poison.Poison

object AntidotePlusPotion : PotionHandler {

    override val itemIds = listOf(
        Items.ANTIDOTE1,
        Items.ANTIDOTE3,
        Items.ANTIDOTE2,
        Items.ANTIDOTE4
    )

    private val replacementMap = mapOf(
        Items.ANTIDOTE4 to Items.ANTIDOTE3,
        Items.ANTIDOTE3 to Items.ANTIDOTE2,
        Items.ANTIDOTE2 to Items.ANTIDOTE1,
        Items.ANTIDOTE1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        Poison.cureWithPotion(player, itemId)

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
