package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object SuperDefencePotion : PotionHandler {

    override val itemIds = listOf(
        Items.SUPER_DEFENCE4,
        Items.SUPER_DEFENCE3,
        Items.SUPER_DEFENCE2,
        Items.SUPER_DEFENCE1
    )

    private val replacementMap = mapOf(
        Items.SUPER_DEFENCE4 to Items.SUPER_DEFENCE3,
        Items.SUPER_DEFENCE3 to Items.SUPER_DEFENCE2,
        Items.SUPER_DEFENCE2 to Items.SUPER_DEFENCE1,
        Items.SUPER_DEFENCE1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp = player.getSkills()
        val baseLevel  = skillsComp.getBaseLevel(Skills.DEFENCE)

        val boost = 5 + (baseLevel * 15) / 100

        skillsComp.incrementCurrentLevel(
            skill  = Skills.DEFENCE,
            value  = boost,
            capped = false
        )

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
