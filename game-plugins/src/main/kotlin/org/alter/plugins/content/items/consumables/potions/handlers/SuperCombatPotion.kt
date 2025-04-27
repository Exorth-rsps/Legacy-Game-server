package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object SuperCombatPotion : PotionHandler {

    override val itemIds = listOf(
        Items.SUPER_COMBAT_POTION4,
        Items.SUPER_COMBAT_POTION3,
        Items.SUPER_COMBAT_POTION2,
        Items.SUPER_COMBAT_POTION1
    )

    private val replacementMap = mapOf(
        Items.SUPER_COMBAT_POTION4 to Items.SUPER_COMBAT_POTION3,
        Items.SUPER_COMBAT_POTION3 to Items.SUPER_COMBAT_POTION2,
        Items.SUPER_COMBAT_POTION2 to Items.SUPER_COMBAT_POTION1,
        Items.SUPER_COMBAT_POTION1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp = player.getSkills()

        listOf(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE).forEach { skillId ->
            val baseLevel = skillsComp.getBaseLevel(skillId)
            val boost = 5 + (baseLevel * 15) / 100
            skillsComp.incrementCurrentLevel(
                skill  = skillId,
                value  = boost,
                capped = false
            )
        }

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(SuperCombatPotion.replacementMap.getValue(itemId), 1)

    }
}
