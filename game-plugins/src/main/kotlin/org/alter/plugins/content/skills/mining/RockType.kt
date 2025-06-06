package org.alter.plugins.content.skills.mining

import org.alter.api.Skills
import org.alter.api.cfg.Items
import org.alter.api.cfg.Objs
import org.alter.api.ext.filterableMessage
import org.alter.game.fs.def.ItemDef
import org.alter.game.model.entity.Player

enum class RockType(
    val level: Int, val experience: Double, val reward: Int, val respawnDelay: Int, val varrockArmourAffected: Int,
    val lowChance: Int, val highChance: Int, val objectIds: Array<Int>, val isGemRock: Boolean = false,
    val drop: Player.() -> Unit = {
        inventory.add(reward)
        addXp(Skills.MINING, experience)
        filterableMessage("You manage to mine some ${world.definitions.get(ItemDef::class.java, reward).name.lowercase()}.")
    }
) {
    ESSENCE(
        level = 1,
        experience = 5.0,
        reward = Items.RUNE_ESSENCE,
        respawnDelay = 0,
        varrockArmourAffected = -1,
        lowChance = 255,
        highChance = 392,
        objectIds = arrayOf(Objs.RUNE_ESSENCE_34773)
    ),
    CLAY(
        level = 1,
        experience = 5.0,
        reward = Items.CLAY,
        respawnDelay = 2,
        varrockArmourAffected = Items.VARROCK_ARMOUR_1,
        lowChance = 128,
        highChance = 392,
        objectIds = arrayOf(Objs.CLAY_ROCKS_11363, Objs.CLAY_ROCKS)
    ),
    COPPER(
        level = 1,
        experience = 17.5,
        reward = Items.COPPER_ORE,
        respawnDelay = 4,
        varrockArmourAffected = Items.VARROCK_ARMOUR_1,
        lowChance = 102,
        highChance = 379,
        objectIds = arrayOf(Objs.COPPER_ROCKS_11161, Objs.COPPER_ROCKS_10943)
    ),
    TIN(
        level = 1,
        experience = 17.5,
        reward = Items.TIN_ORE,
        respawnDelay = 4,
        varrockArmourAffected = Items.VARROCK_ARMOUR_1,
        lowChance = 102,
        highChance = 379,
        objectIds = arrayOf(Objs.TIN_ROCKS_11360, Objs.TIN_ROCKS_11361)
    ),
    IRON(
        level = 15,
        experience = 35.0,
        reward = Items.IRON_ORE,
        respawnDelay = 9,
        varrockArmourAffected = Items.VARROCK_ARMOUR_1,
        lowChance = 109,
        highChance = 346,
        objectIds = arrayOf(Objs.IRON_ROCKS, Objs.IRON_ROCKS_11365)
    ),
    SILVER(
        level = 20,
        experience = 40.0,
        reward = Items.SILVER_ORE,
        respawnDelay = 60,
        varrockArmourAffected = Items.VARROCK_ARMOUR_1,
        lowChance = 109,
        highChance = 346,
        objectIds = arrayOf(Objs.SILVER_ROCKS, Objs.SILVER_ROCKS_11369, Objs.SILVER_ROCKS_36205)
    ),
    COAL(
        level = 30,
        experience = 50.0,
        reward = Items.COAL,
        respawnDelay = 50,
        varrockArmourAffected = Items.VARROCK_ARMOUR_1,
        lowChance = 15,
        highChance = 100,
        objectIds = arrayOf(Objs.COAL_ROCKS_11366, Objs.COAL_ROCKS_11367)
    ),
    GOLD(
        level = 40,
        experience = 65.0,
        reward = Items.GOLD_ORE,
        respawnDelay = 60,
        varrockArmourAffected = Items.VARROCK_ARMOUR_1,
        lowChance = 15,
        highChance = 100,
        objectIds = arrayOf(Objs.GOLD_ROCKS, Objs.GOLD_ROCKS_11371, Objs.GOLD_ROCKS_36206)
    ),
    GEMS(
        level = 40,
        experience = 65.0,
        reward = Items.UNCUT_DIAMOND,   // dummy, wordt niet gebruikt
        respawnDelay = 60,
        varrockArmourAffected= Items.VARROCK_ARMOUR_1,
        objectIds = arrayOf(Objs.GEM_ROCK, Objs.GEM_ROCKS, Objs.GEM_ROCKS_11381),
        isGemRock = true,
        lowChance = 15,
        highChance = 100,
        drop = { GemPlugin.dropGem(this) }
    ),
    MITHRIL(
        level = 55,
        experience = 80.0,
        reward = Items.MITHRIL_ORE,
        respawnDelay = 200,
        varrockArmourAffected = Items.VARROCK_ARMOUR_2,
        lowChance = 2,
        highChance = 50,
        objectIds = arrayOf(Objs.MITHRIL_ROCKS, Objs.MITHRIL_ROCKS_11373)
    ),
    ADAMANTITE(
        level = 70,
        experience = 95.0,
        reward = Items.ADAMANTITE_ORE,
        respawnDelay = 400,
        varrockArmourAffected = Items.VARROCK_ARMOUR_2,
        lowChance = 2,
        highChance = 50,
        objectIds = arrayOf(Objs.ADAMANTITE_ROCKS, Objs.ADAMANTITE_ROCKS_11375, Objs.ADAMANTITE_ROCKS_36208)
    ),
    RUNITE(
        level = 85,
        experience = 125.0,
        reward = Items.RUNITE_ORE,
        respawnDelay = 600,
        varrockArmourAffected = Items.VARROCK_ARMOUR_2,
        lowChance = 1,
        highChance = 25,
        objectIds = arrayOf(Objs.RUNITE_ROCKS_11376, Objs.RUNITE_ROCKS_11377)
    ),
    AMETHYST(
        level = 92,
        experience = 240.0,
        reward = Items.AMETHYST,
        respawnDelay = 600,
        varrockArmourAffected = Items.VARROCK_ARMOUR_2,
        lowChance = 1,
        highChance = 25,
        objectIds = arrayOf(Objs.AMETHYST_CRYSTALS, Objs.AMETHYST_CRYSTALS_11389)
    );


    companion object {
        val values = enumValues<RockType>()
        val objects = RockType.values().flatMap { it.objectIds.toList() }
    }
}