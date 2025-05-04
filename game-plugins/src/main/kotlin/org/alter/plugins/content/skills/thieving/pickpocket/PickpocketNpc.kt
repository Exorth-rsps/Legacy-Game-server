package org.alter.plugins.content.skills.thieving.pickpocket

import org.alter.game.model.weight.impl.WeightItem
import org.alter.game.model.weight.impl.WeightItemSet
import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs

//Rarity weighting
private const val ALWAYS = 0
private const val COMMON = 256
private const val UNCOMMON = 32
private const val RARE = 8
private const val VERY_RARE = 1

/**
 * @property npcIds an array of NPC npcIds for them to pickpocket
 * @property experience the amount of experienceerience given per pickpocket
 * @property reqLevel the level requirement to pickpocket that npc
 * @property rewards a weighted set of possible item rewards
 * @property damage damage range when getting stunned
 * @property stunTicks the amount of time that the npc stuns the player for
 */
enum class PickpocketNpc(val npcIds: IntArray, val experience: Double, val reqLevel: Int, val npcName: String? = null,
                         val rewards: Array<WeightItem>, val damage: IntRange, val stunTicks: Int) {
    MAN_WOMAN(
        npcIds = intArrayOf(
            Npcs.MAN_3106, Npcs.MAN_3107, Npcs.WOMAN_3111, Npcs.WOMAN_3112
        ),
        experience = 8.0,
        reqLevel = 1,
        rewards = arrayOf(
            WeightItem(item = Items.COINS_995, amount = 1, weight = ALWAYS),
            WeightItem(item = Items.COINS_995, amount = 3..12, weight = COMMON),
            WeightItem(item = Items.COINS_995, amount = 12..25, weight = RARE)
        ),
        damage = 1..1,
        stunTicks = 5
    ),
    FARMER(
            npcIds = intArrayOf(
                    Npcs.FARMER_3243, Npcs.FARMER_3244, Npcs.FARMER_3114
            ),
            experience = 14.5,
            reqLevel = 10,
            rewards = arrayOf(
                    WeightItem(item = Items.FEATHER, amount = 1, weight = ALWAYS),
                    WeightItem(item = Items.FEATHER, amount = 3..12, weight = COMMON),
                    WeightItem(item = Items.FEATHER, amount = 12..25, weight = RARE)
            ),
            damage = 1..1,
            stunTicks = 5
    ),
    MASTER_FARMER(
        npcIds = intArrayOf(
            Npcs.MASTER_FARMER_5730, Npcs.MASTER_FARMER_5731, Npcs.MARTIN_THE_MASTER_GARDENER
        ),
        experience = 43.0,
        reqLevel = 38,
        rewards = arrayOf(
            WeightItem(item = Items.FEATHER, amount = 1..4, weight = ALWAYS),
            WeightItem(item = Items.FEATHER, amount = 4..12, weight = COMMON),
            WeightItem(item = Items.FEATHER, amount = 12..20, weight = UNCOMMON),
            WeightItem(item = Items.FEATHER, amount = 20..50, weight = RARE),
            WeightItem(item = Items.FEATHER, amount = 50..100, weight = VERY_RARE)
        ),
        damage = 1..3,
        stunTicks = 5
    ),
    GUARD(
        npcIds = intArrayOf(
            Npcs.GUARD_3269
        ),
        experience = 46.8,
        reqLevel = 40,
        rewards = arrayOf(
            WeightItem(item = Items.BREAD, amount = 1, weight = ALWAYS),
            WeightItem(item = Items.COINS_995, amount = 1..60, weight = ALWAYS),
            WeightItem(item = Items.COINS_995, amount = 60..120, weight = UNCOMMON),
        ),
        damage = 1..2,
        stunTicks = 5
    ),
    WATCHMAN(
        npcIds = intArrayOf(
            Npcs.WATCHMAN_5420
        ),
        experience = 137.5,
        reqLevel = 65,
        rewards = arrayOf(
            WeightItem(item = Items.BREAD, amount = 1, weight = ALWAYS),
            WeightItem(item = Items.COINS_995, amount = 1..60, weight = ALWAYS),
            WeightItem(item = Items.COINS_995, amount = 60..120, weight = UNCOMMON),
        ),
        damage = 1..3,
        stunTicks = 5
    ),
    PALADIN(
        npcIds = intArrayOf(
            Npcs.PALADIN_3293
        ),
        experience = 131.8,
        reqLevel = 80,
        rewards = arrayOf(
            WeightItem(item = Items.COINS_995, amount = 1..200, weight = COMMON),
            WeightItem(item = Items.DEATH_RUNE, amount = 1..2, weight = UNCOMMON),
            WeightItem(item = Items.BLOOD_RUNE, amount = 1..2, weight = RARE),
            WeightItem(item = Items.UNCUT_RUBY, amount = 1, weight = RARE),
            WeightItem(item = Items.COAL, amount = 1, weight = VERY_RARE),
        ),
        damage = 1..4,
        stunTicks = 8
    ),
    HERO(
        npcIds = intArrayOf(
            Npcs.HERO_3295
        ),
        experience = 163.3,
        reqLevel = 80,
        rewards = arrayOf(
            WeightItem(item = Items.COINS_995, amount = 1..250, weight = COMMON),
            WeightItem(item = Items.DEATH_RUNE, amount = 2..8, weight = UNCOMMON),
            WeightItem(item = Items.BLOOD_RUNE, amount = 2..8, weight = RARE),
            WeightItem(item = Items.UNCUT_DIAMOND, amount = 1, weight = RARE),
            WeightItem(item = Items.GOLD_ORE, amount = 1, weight = VERY_RARE),
        ),
        damage = 1..4,
        stunTicks = 8
    );


    val rewardSet = WeightItemSet().apply { rewards.forEach { reward -> add(reward) } }

    companion object {
        val values = enumValues<PickpocketNpc>()
    }
}