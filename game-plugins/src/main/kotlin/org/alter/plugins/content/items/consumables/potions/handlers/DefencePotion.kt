package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object DefencePotion : PotionHandler {

    override val itemIds = listOf(
        Items.DEFENCE_POTION4,
        Items.DEFENCE_POTION3,
        Items.DEFENCE_POTION2,
        Items.DEFENCE_POTION1
    )

    private val replacementMap = mapOf(
        Items.DEFENCE_POTION4 to Items.DEFENCE_POTION3,
        Items.DEFENCE_POTION3 to Items.DEFENCE_POTION2,
        Items.DEFENCE_POTION2 to Items.DEFENCE_POTION1,
        Items.DEFENCE_POTION1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val baseLevel = player.getSkills().getBaseLevel(Skills.DEFENCE)
        val boost = 3 + (baseLevel / 10)

        player.getSkills().incrementCurrentLevel(
            skill  = Skills.DEFENCE,
            value  = boost,
            capped = false
        )

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
