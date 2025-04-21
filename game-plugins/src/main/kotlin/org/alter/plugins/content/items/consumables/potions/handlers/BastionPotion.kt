package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler


object BastionPotion : PotionHandler {

    override val itemIds = listOf(
        Items.BASTION_POTION4,
        Items.BASTION_POTION3,
        Items.BASTION_POTION2,
        Items.BASTION_POTION1
    )

    private val replacementMap = mapOf(
        Items.BASTION_POTION4 to Items.BASTION_POTION3,
        Items.BASTION_POTION3 to Items.BASTION_POTION2,
        Items.BASTION_POTION2 to Items.BASTION_POTION1,
        Items.BASTION_POTION1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp   = player.getSkills()
        val baseRanged   = skillsComp.getBaseLevel(Skills.RANGED)
        val boostRanged  = 4 + (baseRanged / 10)
        skillsComp.incrementCurrentLevel(
            skill  = Skills.RANGED,
            value  = boostRanged,
            capped = false
        )

        val baseDef      = skillsComp.getBaseLevel(Skills.DEFENCE)
        val boostDefence = 5 + (baseDef * 15) / 100
        skillsComp.incrementCurrentLevel(
            skill  = Skills.DEFENCE,
            value  = boostDefence,
            capped = false
        )

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
