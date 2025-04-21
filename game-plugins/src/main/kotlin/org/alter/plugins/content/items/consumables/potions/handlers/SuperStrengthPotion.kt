package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler


object SuperStrengthPotion : PotionHandler {

    override val itemIds = listOf(
        Items.SUPER_STRENGTH4,
        Items.SUPER_STRENGTH3,
        Items.SUPER_STRENGTH2,
        Items.SUPER_STRENGTH1
    )

    private val replacementMap = mapOf(
        Items.SUPER_STRENGTH4 to Items.SUPER_STRENGTH3,
        Items.SUPER_STRENGTH3 to Items.SUPER_STRENGTH2,
        Items.SUPER_STRENGTH2 to Items.SUPER_STRENGTH1,
        Items.SUPER_STRENGTH1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp = player.getSkills()
        val baseLevel  = skillsComp.getBaseLevel(Skills.STRENGTH)

        val boost = 5 + (baseLevel * 15) / 100

        skillsComp.incrementCurrentLevel(
            skill  = Skills.STRENGTH,
            value  = boost,
            capped = false
        )

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)
    }
}
