package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.SOLDIER_5421
)

val table = DropTableFactory
val soldier =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
            obj(Items.COINS_995, quantityRange = 5..10)
        }
        main {
            total(256)
            obj(Items.STEEL_FULL_HELM, quantity = 1, slots = 1)
            obj(Items.STEEL_CHAINBODY, quantity = 1, slots = 2)
            obj(Items.STEEL_PLATELEGS, quantity = 1, slots = 1)
            obj(Items.STEEL_PLATESKIRT, quantity = 1, slots = 1)
            obj(Items.STEEL_PLATEBODY, quantity = 1, slots = 1)
            obj(Items.STEEL_SCIMITAR, quantity = 1, slots = 1)
            obj(Items.STEEL_MED_HELM, quantity = 1, slots = 2)
            obj(Items.STEEL_SWORD, quantity = 1, slots = 2)
            obj(Items.STEEL_2H_SWORD, quantity = 1, slots = 1)
            obj(Items.STEEL_BATTLEAXE, quantity = 1, slots = 1)
            nothing(8)
        }
        table("Herbs") {
            total(256)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, slots = 2)
            obj(Items.GRIMY_HARRALANDER, quantity = 1, slots = 1)
            nothing(slots = 32)
        }
    }


table.register(soldier, *ids)

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
            hitpoints = 22
            attack = 26
            strength = 25
            defence = 26
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 24
            defenceSlash = 14
            defenceCrush = 19
            defenceMagic = -4
            defenceRanged = 16
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