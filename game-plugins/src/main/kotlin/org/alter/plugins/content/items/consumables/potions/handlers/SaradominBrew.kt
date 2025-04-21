package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object SaradominBrew : PotionHandler {

    override val itemIds = listOf(
        Items.SARADOMIN_BREW4,
        Items.SARADOMIN_BREW3,
        Items.SARADOMIN_BREW2,
        Items.SARADOMIN_BREW1
    )

    private val replacementMap = mapOf(
        Items.SARADOMIN_BREW4 to Items.SARADOMIN_BREW3,
        Items.SARADOMIN_BREW3 to Items.SARADOMIN_BREW2,
        Items.SARADOMIN_BREW2 to Items.SARADOMIN_BREW1,
        Items.SARADOMIN_BREW1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skills = player.getSkills()

        val baseHp    = skills.getBaseLevel(Skills.HITPOINTS)
        val baseDef   = skills.getBaseLevel(Skills.DEFENCE)
        val baseAtk   = skills.getBaseLevel(Skills.ATTACK)
        val baseStr   = skills.getBaseLevel(Skills.STRENGTH)
        val baseRng   = skills.getBaseLevel(Skills.RANGED)
        val baseMag   = skills.getBaseLevel(Skills.MAGIC)

        val hpBoost   = (baseHp * 15) / 100 + 2
        val defBoost  = (baseDef * 20) / 100 + 2
        fun debuff(base: Int) = (base * 10) / 100 + 2

        skills.incrementCurrentLevel(Skills.HITPOINTS, hpBoost, capped = false)
        skills.incrementCurrentLevel(Skills.DEFENCE,  defBoost, capped = false)

        skills.incrementCurrentLevel(Skills.ATTACK,   -debuff(baseAtk), capped = false)
        skills.incrementCurrentLevel(Skills.STRENGTH, -debuff(baseStr), capped = false)
        skills.incrementCurrentLevel(Skills.MAGIC,    -debuff(baseMag), capped = false)
        skills.incrementCurrentLevel(Skills.RANGED,   -debuff(baseRng), capped = false)

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
