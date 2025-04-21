package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

object PrayerPotion : PotionHandler {

    override val itemIds = listOf(
        Items.PRAYER_POTION4,
        Items.PRAYER_POTION3,
        Items.PRAYER_POTION2,
        Items.PRAYER_POTION1
    )

    private val replacementMap = mapOf(
        Items.PRAYER_POTION4 to Items.PRAYER_POTION3,
        Items.PRAYER_POTION3 to Items.PRAYER_POTION2,
        Items.PRAYER_POTION2 to Items.PRAYER_POTION1,
        Items.PRAYER_POTION1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp = player.getSkills()
        val currentPrayer = skillsComp.getCurrentLevel(Skills.PRAYER)

        val boost = 7 + (currentPrayer / 4)

        skillsComp.incrementCurrentLevel(
            skill  = Skills.PRAYER,
            value  = boost,
            capped = true
        )

        player.animate(829)
        player.graphic(149)

        // fles vervangen
        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
