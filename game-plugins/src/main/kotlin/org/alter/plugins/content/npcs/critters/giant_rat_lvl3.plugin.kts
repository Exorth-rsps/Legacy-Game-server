package org.alter.plugins.content.npcs.critters;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.GIANT_RAT_2858
)

val table = DropTableFactory
val soldier =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
            obj(Items.RAW_RAT_MEAT, quantity = 1)
        }
        main {
            total(256)
            nothing(slots = 32)
            obj(Items.COINS_995, quantityRange = 5..10, slots = 8)
            obj(Items.IRON_FULL_HELM, quantity = 1, slots = 1)
            obj(Items.IRON_MED_HELM, quantity = 1, slots = 2)
            obj(Items.IRON_PLATEBODY, quantity = 1, slots = 1)
            obj(Items.IRON_PLATELEGS, quantity = 1, slots = 1)
            obj(Items.IRON_PLATESKIRT, quantity = 1, slots = 1)
            obj(Items.IRON_CHAINBODY, quantity = 1, slots = 2)
            obj(Items.IRON_2H_SWORD, quantity = 1, slots = 1)
            obj(Items.IRON_SCIMITAR, quantity = 1, slots = 1)
        }
        table("Herbs") {
            total(256)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, slots = 1)
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
            attackSpeed = 4
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 5
            attack = 2
            strength = 3
            defence = 2
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 0
            defenceSlash = 0
            defenceCrush = 0
            defenceMagic = 0
            defenceRanged = 0
        }


        anims {
            attack = Animation.GIANT_CRYPT_RAT_ATTACK
            block = Animation.GIANT_CRYPT_RAT_DEFEND
            death = Animation.GIANT_CRYPT_RAT_DEATH
        }

        sound {
            attackSound = Sound.RAT_ATTACK
            blockSound = Sound.RAT_HIT
            deathSound = Sound.RAT_DEATH
        }
    }
}