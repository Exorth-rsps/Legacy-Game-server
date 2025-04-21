package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object SuperAttackPotion : PotionHandler {

    override val itemIds = listOf(
        Items.SUPER_ATTACK4,
        Items.SUPER_ATTACK3,
        Items.SUPER_ATTACK2,
        Items.SUPER_ATTACK1
    )

    private val replacementMap = mapOf(
        Items.SUPER_ATTACK4 to Items.SUPER_ATTACK3,
        Items.SUPER_ATTACK3 to Items.SUPER_ATTACK2,
        Items.SUPER_ATTACK2 to Items.SUPER_ATTACK1,
        Items.SUPER_ATTACK1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp = player.getSkills()
        val baseLevel  = skillsComp.getBaseLevel(Skills.ATTACK)

        val boost = 5 + (baseLevel * 15) / 100

        skillsComp.incrementCurrentLevel(
            skill  = Skills.ATTACK,
            value  = boost,
            capped = false
        )

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
