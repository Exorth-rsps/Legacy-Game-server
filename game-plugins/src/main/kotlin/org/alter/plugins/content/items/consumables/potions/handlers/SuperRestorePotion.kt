// src/main/kotlin/org/alter/plugins/content/items/consumables/potions/handlers/SuperRestorePotion.kt
package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

/**
 * Eén handler voor SUPER_RESTORE4 → 3 → 2 → 1 → VIAL
 * Cures stat drains (behalve Hitpoints) en restores Prayer.
 * Voor élke skill: boost = floor(baseLevel * 0.25) + 8.
 */
object SuperRestorePotion : PotionHandler {

    override val itemIds = listOf(
        Items.SUPER_RESTORE4,
        Items.SUPER_RESTORE3,
        Items.SUPER_RESTORE2,
        Items.SUPER_RESTORE1
    )

    private val replacementMap = mapOf(
        Items.SUPER_RESTORE4 to Items.SUPER_RESTORE3,
        Items.SUPER_RESTORE3 to Items.SUPER_RESTORE2,
        Items.SUPER_RESTORE2 to Items.SUPER_RESTORE1,
        Items.SUPER_RESTORE1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp = player.getSkills()

        listOf(
            Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE,
            Skills.RANGED, Skills.MAGIC, Skills.COOKING,
            Skills.WOODCUTTING, Skills.FLETCHING, Skills.FISHING,
            Skills.FIREMAKING, Skills.CRAFTING, Skills.SMITHING,
            Skills.MINING, Skills.HERBLORE, Skills.AGILITY,
            Skills.THIEVING, Skills.SLAYER, Skills.FARMING,
            Skills.RUNECRAFTING, Skills.HUNTER, Skills.CONSTRUCTION,
            Skills.PRAYER
        ).forEach { skillId ->
            val base    = skillsComp.getBaseLevel(skillId)
            val current = skillsComp.getCurrentLevel(skillId)
            if (current < base) {
                val boost = (base / 4) + 8
                skillsComp.incrementCurrentLevel(
                    skill  = skillId,
                    value  = boost,
                    capped = true
                )
            }
        }

        player.animate(829)

        player.inventory.remove(itemId, 1)
        player.inventory.add(replacementMap.getValue(itemId), 1)

    }
}
