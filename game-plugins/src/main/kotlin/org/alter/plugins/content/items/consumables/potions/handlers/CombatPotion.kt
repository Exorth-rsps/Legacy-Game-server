package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler


object CombatPotion : PotionHandler {

    override val itemIds = listOf(
        Items.COMBAT_POTION4,
        Items.COMBAT_POTION3,
        Items.COMBAT_POTION2,
        Items.COMBAT_POTION1
    )

    private val replacementMap = mapOf(
        Items.COMBAT_POTION4 to Items.COMBAT_POTION3,
        Items.COMBAT_POTION3 to Items.COMBAT_POTION2,
        Items.COMBAT_POTION2 to Items.COMBAT_POTION1,
        Items.COMBAT_POTION1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp = player.getSkills()

        listOf(Skills.ATTACK, Skills.STRENGTH).forEach { skillId ->
            val baseLevel = skillsComp.getBaseLevel(skillId)
            val boost = 3 + (baseLevel / 10)
            skillsComp.incrementCurrentLevel(
                skill  = skillId,
                value  = boost,
                capped = false
            )
        }

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
