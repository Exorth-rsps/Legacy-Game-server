// src/main/kotlin/org/alter/plugins/content/items/consumables/potions/handlers/RestorePotion.kt
package org.alter.plugins.content.items.consumables.potions.handlers

import org.alter.api.cfg.Items
import org.alter.api.Skills
import org.alter.game.model.entity.Player
import org.alter.plugins.content.items.consumables.potions.PotionHandler

/**
 * Eén handler voor RESTORE_POTION4 → 3 → 2 → 1 → VIAL
 * Restore potion: herstelt alle verlaagde stats (Attack, Strength,
 * Defence, Ranged, Magic) met “10 + 30% van je base level”, tot max je base.
 */
object RestorePotion : PotionHandler {

    override val itemIds = listOf(
        Items.RESTORE_POTION4,
        Items.RESTORE_POTION3,
        Items.RESTORE_POTION2,
        Items.RESTORE_POTION1
    )

    private val replacementMap = mapOf(
        Items.RESTORE_POTION4 to Items.RESTORE_POTION3,
        Items.RESTORE_POTION3 to Items.RESTORE_POTION2,
        Items.RESTORE_POTION2 to Items.RESTORE_POTION1,
        Items.RESTORE_POTION1 to Items.VIAL
    )

    override fun onDrink(player: Player, itemId: Int) {
        val skillsComp = player.getSkills()


        listOf(
            Skills.ATTACK,
            Skills.STRENGTH,
            Skills.DEFENCE,
            Skills.RANGED,
            Skills.MAGIC
        ).forEach { skillId ->
            val base    = skillsComp.getBaseLevel(skillId)
            val current = skillsComp.getCurrentLevel(skillId)


            if (current < base) {

                val boost = (base * 3) / 10 + 10

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
