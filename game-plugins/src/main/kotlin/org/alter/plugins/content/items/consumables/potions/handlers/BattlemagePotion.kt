package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object BattlemagePotion : PotionHandler {

    override val itemIds = listOf(
        Items.BATTLEMAGE_POTION4,
        Items.BATTLEMAGE_POTION3,
        Items.BATTLEMAGE_POTION2,
        Items.BATTLEMAGE_POTION1
    )

    private val replacementMap = mapOf(
        Items.BATTLEMAGE_POTION4 to Items.BATTLEMAGE_POTION3,
        Items.BATTLEMAGE_POTION3 to Items.BATTLEMAGE_POTION2,
        Items.BATTLEMAGE_POTION2 to Items.BATTLEMAGE_POTION1,
        Items.BATTLEMAGE_POTION1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp = player.getSkills()

        skillsComp.incrementCurrentLevel(
            skill  = Skills.MAGIC,
            value  = 4,
            capped = false
        )

        val baseDef   = skillsComp.getBaseLevel(Skills.DEFENCE)
        val boostDef  = 5 + (baseDef * 15) / 100
        skillsComp.incrementCurrentLevel(
            skill  = Skills.DEFENCE,
            value  = boostDef,
            capped = false
        )

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
