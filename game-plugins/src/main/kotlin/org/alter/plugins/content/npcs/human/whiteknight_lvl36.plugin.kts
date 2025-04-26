package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.WHITE_KNIGHT
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(1024)
            obj(Items.STEEL_SWORD, quantity = 1, slots = 8)
            obj(Items.STEEL_MED_HELM, quantity = 1, slots = 8)
            obj(Items.STEEL_ARROW, quantityRange = 10..100, slots = 8)
            obj(Items.WILLOW_SHORTBOW, quantity = 1, slots = 4)
            obj(Items.MIND_RUNE, quantityRange = 1..75, slots = 4)
            obj(Items.CHAOS_RUNE, quantityRange = 1..25, slots = 4)
            obj(Items.MITHRIL_ARROW, quantityRange = 1..50, slots = 2)
            obj(Items.COIF, quantity = 1, slots = 2)
            obj(Items.LEATHER_BODY, quantity = 1, slots = 2)
            obj(Items.STUDDED_BODY, quantity = 1, slots = 2)
            obj(Items.LEATHER_CHAPS, quantity = 1, slots = 2)
            obj(Items.STUDDED_CHAPS, quantity = 1, slots = 2)
            obj(Items.LEATHER_VAMBRACES, quantity = 1, slots = 2)
            obj(Items.LEATHER_BOOTS, quantity = 1, slots = 4)
            obj(Items.DESERT_BOOTS, quantity = 1, slots = 1)
            obj(Items.COOKING_GAUNTLETS, quantity = 1, slots = 1)
            obj(Items.WATER_RUNE, quantityRange = 1..50, slots = 2)
            obj(Items.WHITE_MED_HELM, quantity = 1, slots = 2)
            obj(Items.WHITE_SWORD, quantity = 1, slots = 2)
            obj(Items.WHITE_CHAINBODY, quantity = 1, slots = 2)
            obj(Items.WHITE_FULL_HELM, quantity = 1, slots = 1)
            obj(Items.WHITE_PLATELEGS, quantity = 1, slots = 1)
            obj(Items.WHITE_PLATESKIRT, quantity = 1, slots = 1)
            obj(Items.WHITE_KITESHIELD, quantity = 1, slots = 1)
            obj(Items.WHITE_PLATEBODY, quantity = 1, slots = 1)
            nothing(slots = 128)
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
            attackSpeed = 7
            respawnDelay = 60
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 52
            attack = 27
            strength = 29
            defence = 21
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 83
            defenceSlash = 76
            defenceCrush = 70
            defenceMagic = -11
            defenceRanged = 74
        }


        anims {
            attack = Animation.HUMAN_2H_SWORD_ATTACK
            block = Animation.HUMAN_2H_STRAIGHT_SWORD_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_BLOCK_1
            deathSound = Sound.HUMAN_DEATH
        }
    }
}