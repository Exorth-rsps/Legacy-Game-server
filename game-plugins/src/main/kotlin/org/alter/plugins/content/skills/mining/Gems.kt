package org.alter.plugins.content.skills.mining

import org.alter.api.cfg.Items
import org.alter.api.ext.filterableMessage
import org.alter.game.fs.def.ItemDef
import org.alter.game.model.entity.Player
import org.alter.api.Skills
import org.alter.api.cfg.Objs
import kotlin.random.Random

/**
 * Plugin for handling gem rock drops.
 * Defines gem rock object IDs, required mining level, xp and rarity table.
 * Bonus unidentified minerals do NOT apply to gem rocks.
 */
object GemPlugin {

    // === Config ===
    // IDs of all gem rock objects in the world
    val GEM_ROCK_OBJECT_IDS = arrayOf(
        Objs.GEM_ROCK,
        Objs.GEM_ROCKS,
        Objs.GEM_ROCKS_11381
    )

    // Required Mining level to mine gem rocks
    const val REQUIRED_LEVEL = 40

    // XP awarded per gem mined
    const val EXPERIENCE = 65.0

    // Rarity table: probability = 1 / rarity
    private val rarityMap = mapOf(
        Items.UNCUT_OPAL to 2.133,
        Items.UNCUT_JADE to 4.267,
        Items.UNCUT_RED_TOPAZ to 8.533,
        Items.UNCUT_SAPPHIRE to 14.22,
        Items.UNCUT_EMERALD to 25.6,
        Items.UNCUT_RUBY to 25.6,
        Items.UNCUT_DIAMOND to 32.0
    )

    // Cached arrays for weighted random
    private val rewards = rarityMap.keys.toTypedArray()
    private val weights = rewards.map { 1.0 / (rarityMap[it] ?: 1.0) }.toDoubleArray()

    /**
     * Rolls one gem based on the weight table.
     */
    private fun pickGem(): Int {
        val total = weights.sum()
        var r = Random.nextDouble() * total
        for (i in weights.indices) {
            r -= weights[i]
            if (r <= 0.0) {
                return rewards[i]
            }
        }
        return rewards.last()
    }

    /**
     * Handles gem drop for a successful gem rock mine.
     * Adds the gem to inventory, awards XP and sends a message.
     */
    fun dropGem(player: Player) {
        val gemId = pickGem()
        player.inventory.add(gemId)
        player.addXp(Skills.MINING, EXPERIENCE)

        // Send feedback
        val gemName = player.world.definitions.get(ItemDef::class.java, gemId).name.lowercase()
        player.filterableMessage("You manage to mine some $gemName.")
    }
}
