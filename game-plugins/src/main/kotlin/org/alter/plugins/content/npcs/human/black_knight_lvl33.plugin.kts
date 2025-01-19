package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.BLACK_KNIGHT
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.COINS_995, quantityRange = 1..5, slots = 16)
            obj(Items.COINS_995, quantityRange = 15..45, slots = 2)
            obj(Items.BLACK_SWORD, quantity = 1, slots = 1)
            obj(Items.BLACK_AXE, quantity = 1, slots = 1)
            obj(Items.IRON_SWORD, quantity = 1, slots = 16)
            obj(Items.STEEL_MACE, quantity = 1, slots = 2)
            obj(Items.LIMPWURT_ROOT, quantity = 1, slots = 4)
            obj(Items.WINE_OF_ZAMORAK, quantity = 1, slots = 2)
            nothing(16)
        }
        table("Herbs") {
            total(512)
            nothing(slots = 4)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, slots = 4)
            obj(Items.GRIMY_MARRENTILL, quantity = 1, slots = 4)
            obj(Items.GRIMY_TARROMIN, quantity = 1, slots = 4)
            obj(Items.GRIMY_HARRALANDER, quantity = 1, slots = 2)
            obj(Items.GRIMY_RANARR_WEED, quantity = 1, slots = 2)
            obj(Items.GRIMY_IRIT_LEAF, quantity = 1, slots = 2)
            obj(Items.GRIMY_AVANTOE, quantity = 1, slots = 1)
            obj(Items.GRIMY_KWUARM, quantity = 1, slots = 1)
            obj(Items.GRIMY_CADANTINE, quantity = 1, slots = 1)
            obj(Items.GRIMY_LANTADYME, quantity = 1, slots = 1)
            obj(Items.GRIMY_DWARF_WEED, quantity = 1, slots = 1)
        }
        table("Ammunition") {
            total(1024)
            obj(Items.IRON_ARROW, quantityRange = 1..25, slots = 8)
            obj(Items.STEEL_ARROW, quantityRange = 1..20, slots = 4)
            obj(Items.MITHRIL_ARROW, quantityRange = 1..15, slots = 2)
            obj(Items.MIND_RUNE, quantityRange = 1..35, slots = 32)
            obj(Items.CHAOS_RUNE, quantityRange = 1..10, slots = 8)
            obj(Items.DEATH_RUNE, quantityRange = 1..8, slots = 4)
            obj(Items.BLOOD_RUNE, quantityRange = 1..5, slots = 2)
            obj(Items.WRATH_RUNE, quantityRange = 1..2, slots = 1)
        }
    }


table.register(droptable, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
}

on_npc_death(*ids) {
    table.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}


ids.forEach {
    set_combat_def(it) {
        configs {
            attackSpeed = 5
            respawnDelay = 50
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 42
            attack = 25
            strength = 25
            defence = 25
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 73
            defenceSlash = 76
            defenceCrush = 70
            defenceMagic = -11
            defenceRanged = 72
        }


        anims {
            attack = Animation.HUMAN_SLASH_SWORD_ATTACK
            block = Animation.HUMAN_SLASH_SWORD_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_BLOCK_1
            deathSound = Sound.HUMAN_DEATH
        }
    }
}