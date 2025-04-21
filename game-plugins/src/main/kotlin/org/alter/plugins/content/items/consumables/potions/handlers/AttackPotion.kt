package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object AttackPotion : PotionHandler {

    override val itemIds = listOf(
        Items.ATTACK_POTION4,
        Items.ATTACK_POTION3,
        Items.ATTACK_POTION2,
        Items.ATTACK_POTION1
    )

    private val replacementMap = mapOf(
        Items.ATTACK_POTION4 to Items.ATTACK_POTION3,
        Items.ATTACK_POTION3 to Items.ATTACK_POTION2,
        Items.ATTACK_POTION2 to Items.ATTACK_POTION1,
        Items.ATTACK_POTION1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val baseLevel = player.getSkills().getBaseLevel(Skills.ATTACK)
        val boost = 3 + (baseLevel / 10)

        player.getSkills().incrementCurrentLevel(
            skill  = Skills.ATTACK,
            value  = boost,
            capped = false
        )

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
