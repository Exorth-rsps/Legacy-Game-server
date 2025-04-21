package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object RangingPotion : PotionHandler {

    override val itemIds = listOf(
        Items.RANGING_POTION4,
        Items.RANGING_POTION3,
        Items.RANGING_POTION2,
        Items.RANGING_POTION1
    )

    private val replacementMap = mapOf(
        Items.RANGING_POTION4 to Items.RANGING_POTION3,
        Items.RANGING_POTION3 to Items.RANGING_POTION2,
        Items.RANGING_POTION2 to Items.RANGING_POTION1,
        Items.RANGING_POTION1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp = player.getSkills()
        val baseLevel  = skillsComp.getBaseLevel(Skills.RANGED)

        val boost = 4 + (baseLevel / 10)

        skillsComp.incrementCurrentLevel(
            skill  = Skills.RANGED,
            value  = boost,
            capped = false
        )

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
