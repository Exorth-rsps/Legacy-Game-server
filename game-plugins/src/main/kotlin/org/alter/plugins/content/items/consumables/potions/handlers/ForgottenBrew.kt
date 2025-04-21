package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler


object ForgottenBrew : PotionHandler {

    override val itemIds = listOf(
        Items.FORGOTTEN_BREW4,
        Items.FORGOTTEN_BREW3,
        Items.FORGOTTEN_BREW2,
        Items.FORGOTTEN_BREW1
    )

    private val replacementMap = mapOf(
        Items.FORGOTTEN_BREW4 to Items.FORGOTTEN_BREW3,
        Items.FORGOTTEN_BREW3 to Items.FORGOTTEN_BREW2,
        Items.FORGOTTEN_BREW2 to Items.FORGOTTEN_BREW1,
        Items.FORGOTTEN_BREW1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skills = player.getSkills()

        val baseMag = skills.getBaseLevel(Skills.MAGIC)
        val basePr  = skills.getBaseLevel(Skills.PRAYER)
        val baseAtk = skills.getBaseLevel(Skills.ATTACK)
        val baseStr = skills.getBaseLevel(Skills.STRENGTH)
        val baseDef = skills.getBaseLevel(Skills.DEFENCE)

        val magicBoost      = 3 + (baseMag * 8) / 100        // 8% van baseMagic
        val prayerRestore   = (basePr * 10) / 100 + 2        // 10% van basePrayer + 2
        val meleeDrainValue = (baseAtk * 10) / 100 + 2       // 10% van base + 2

        skills.incrementCurrentLevel(
            skill  = Skills.MAGIC,
            value  = magicBoost,
            capped = false
        )

        skills.incrementCurrentLevel(
            skill  = Skills.PRAYER,
            value  = prayerRestore,
            capped = true
        )

        listOf(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE).forEach { skillId ->
            skills.incrementCurrentLevel(
                skill  = skillId,
                value  = -meleeDrainValue,
                capped = true
            )
        }

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
