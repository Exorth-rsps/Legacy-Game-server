package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object ZamorakBrew : PotionHandler {

    override val itemIds = listOf(
        Items.ZAMORAK_BREW4,
        Items.ZAMORAK_BREW3,
        Items.ZAMORAK_BREW2,
        Items.ZAMORAK_BREW1
    )

    private val replacementMap = mapOf(
        Items.ZAMORAK_BREW4 to Items.ZAMORAK_BREW3,
        Items.ZAMORAK_BREW3 to Items.ZAMORAK_BREW2,
        Items.ZAMORAK_BREW2 to Items.ZAMORAK_BREW1,
        Items.ZAMORAK_BREW1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skills = player.getSkills()

        val baseAtk    = skills.getBaseLevel(Skills.ATTACK)
        val baseStr    = skills.getBaseLevel(Skills.STRENGTH)
        val baseDef    = skills.getBaseLevel(Skills.DEFENCE)
        val currHp     = skills.getCurrentLevel(Skills.HITPOINTS)
        val currPrayer = skills.getCurrentLevel(Skills.PRAYER)

        val boostAtk   = (baseAtk * 20) / 100 + 2
        val boostStr   = (baseStr * 12) / 100 + 2
        val debuffDef  = (baseDef * 10) / 100 + 2
        val debuffHp   = (currHp * 12) / 100
        val restorePr  = (currPrayer * 10) / 100

        skills.incrementCurrentLevel(Skills.ATTACK,   boostAtk, capped = false)
        skills.incrementCurrentLevel(Skills.STRENGTH, boostStr, capped = false)
        skills.incrementCurrentLevel(Skills.DEFENCE,    -debuffDef, capped = true)
        skills.incrementCurrentLevel(Skills.HITPOINTS,  -debuffHp,   capped = true)
        skills.incrementCurrentLevel(Skills.PRAYER,     restorePr,   capped = true)

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
