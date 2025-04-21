package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object AncientBrew : PotionHandler {

    override val itemIds = listOf(
        Items.ANCIENT_BREW4,
        Items.ANCIENT_BREW3,
        Items.ANCIENT_BREW2,
        Items.ANCIENT_BREW1
    )

    private val replacementMap = mapOf(
        Items.ANCIENT_BREW4 to Items.ANCIENT_BREW3,
        Items.ANCIENT_BREW3 to Items.ANCIENT_BREW2,
        Items.ANCIENT_BREW2 to Items.ANCIENT_BREW1,
        Items.ANCIENT_BREW1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skills = player.getSkills()

        listOf(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE).forEach { skillId ->
            val base = skills.getBaseLevel(skillId)
            val drain = (base * 10) / 100 + 2
            skills.incrementCurrentLevel(
                skill  = skillId,
                value  = -drain,
                capped = true
            )
        }

        val baseMag = skills.getBaseLevel(Skills.MAGIC)
        val boostMag = (baseMag * 5) / 100 + 2
        skills.incrementCurrentLevel(
            skill  = Skills.MAGIC,
            value  = boostMag,
            capped = false
        )

        val basePr  = skills.getBaseLevel(Skills.PRAYER)
        val restore = (basePr * 10) / 100 + 2
        skills.incrementCurrentLevel(
            skill  = Skills.PRAYER,
            value  = restore,
            capped = false
        )

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
