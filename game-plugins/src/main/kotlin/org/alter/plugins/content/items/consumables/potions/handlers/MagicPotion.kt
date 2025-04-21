package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object MagicPotion : PotionHandler {

    override val itemIds = listOf(
        Items.MAGIC_POTION4,
        Items.MAGIC_POTION3,
        Items.MAGIC_POTION2,
        Items.MAGIC_POTION1
    )

    private val replacementMap = mapOf(
        Items.MAGIC_POTION4 to Items.MAGIC_POTION3,
        Items.MAGIC_POTION3 to Items.MAGIC_POTION2,
        Items.MAGIC_POTION2 to Items.MAGIC_POTION1,
        Items.MAGIC_POTION1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp = player.getSkills()
        val baseLevel  = skillsComp.getBaseLevel(Skills.MAGIC)

        val boost = 4 + (baseLevel / 10)

        skillsComp.incrementCurrentLevel(
            skill  = Skills.MAGIC,
            value  = boost,
            capped = false
        )

        // animatie & graphic
        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
