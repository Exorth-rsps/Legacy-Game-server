package org.alter.plugins.content.skills.crafting.action

import org.alter.game.fs.DefinitionSet
import org.alter.game.fs.def.ItemDef
import org.alter.game.model.queue.QueueTask
import org.alter.api.Skills
import org.alter.api.cfg.Items

import org.alter.api.ext.*
import org.alter.plugins.content.skills.crafting.data.Chiseled

/**
 * @author momof513
 *
 * Handles the action of chiseling items with a chisel
 */
class ChiselAction(private val defs: DefinitionSet) {
    /**
     * A map of chiseled ids to their item names
     */
    private val chiseledNames = Chiseled.chiseledDefinitions.keys.associate { it to defs.get(ItemDef::class.java, it).name.lowercase() }

    /**
     * A map of unchiseled item ids to their item names
     */
    private val unchiseledNames = Chiseled.chiseledDefinitions.values.associate { it.unchiseled to defs.get(ItemDef::class.java, it.unchiseled).name.lowercase() }

    /**
     * Handles the chiseling of a chiseled
     *
     * @param task      The queued action task
     * @param chiseled  The Chiseled definition
     * @param amount    The amount the player is trying to chisel
     */
    suspend fun chisel(task: QueueTask, chiseled: org.alter.plugins.content.skills.crafting.data.Chiseled, amount: Int) {
        if (!canChisel(task, chiseled))
            return

        val player = task.player
        val inventory = player.inventory

        val maxCount = Math.min(amount, inventory.getItemCount(chiseled.unchiseled))

        // Wait two ticks to follow OSRS behavior
        task.wait(2)
        var completed = 0
        while(completed < maxCount) {
            player.animate(chiseled.animation)
            task.wait(4)

            player.lock()
            // This is here to prevent a TOCTTOU attack
            if (!canChisel(task, chiseled, sendMessageBox = false)){
                player.unlock()
                break
            }

            val removeUnchiseled = inventory.remove(item = chiseled.unchiseled, amount = 1, assureFullRemoval = true)
            if (removeUnchiseled.hasFailed()){
                player.unlock()
                break
            }

            inventory.add(chiseled.id, amount = chiseled.amount)
            player.addXp(Skills.CRAFTING, chiseled.craftingXP * chiseled.amount)
            completed++
            player.unlock()
        }
    }

    /**
     * Checks if an item can be chiseled into the given chiseled Item
     *
     * @param task              The queued task
     * @param chiseled          The chiseled item being created
     * @param sendMessageBox    Whether or not to send the error message
     */
    private suspend fun canChisel(task: QueueTask, chiseled: org.alter.plugins.content.skills.crafting.data.Chiseled, sendMessageBox: Boolean = true) : Boolean {
        val player = task.player
        val inventory = player.inventory
        if (!inventory.contains(chiseled.unchiseled) || !inventory.contains(Items.CHISEL)) {
            if(sendMessageBox)
                task.messageBox("You need a chisel and ${unchiseledNames[chiseled.unchiseled]} to make ${chiseledNames[chiseled.id]}")
            return false
        }

        if (player.getSkills().getCurrentLevel(Skills.CRAFTING) < chiseled.level) {
            if(sendMessageBox)
                task.messageBox("You need a ${Skills.getSkillName(player.world, Skills.CRAFTING)} level of at least ${chiseled.level} to craft ${chiseledNames[chiseled.id]}.")
            return false
        }

        return true
    }
}