package org.alter.plugins.content.npcs.other;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.GOBLIN_3028,
    Npcs.GOBLIN_3029,
    Npcs.GOBLIN_3030
)

val table = DropTableFactory
val soldier =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(256)
            nothing(slots = 32)
            obj(Items.COINS_995, quantityRange = 1..5, slots = 8)
            obj(Items.BRONZE_FULL_HELM, quantity = 1, slots = 1)
            obj(Items.BRONZE_MED_HELM, quantity = 1, slots = 2)
            obj(Items.BRONZE_PLATEBODY, quantity = 1, slots = 1)
            obj(Items.BRONZE_PLATELEGS, quantity = 1, slots = 1)
            obj(Items.BRONZE_PLATESKIRT, quantity = 1, slots = 1)
            obj(Items.BRONZE_CHAINBODY, quantity = 1, slots = 2)
            obj(Items.BRONZE_2H_SWORD, quantity = 1, slots = 1)
            obj(Items.BRONZE_SCIMITAR, quantity = 1, slots = 1)
            obj(Items.BRONZE_SWORD, quantity = 1, slots = 8)
            obj(Items.BRONZE_LONGSWORD, quantity = 1, slots = 2)
            obj(Items.BRONZE_PICKAXE, quantity = 1, slots = 2)
            obj(Items.BRONZE_AXE, quantity = 1, slots = 2)
            obj(Items.BRONZE_WARHAMMER, quantity = 1, slots = 1)
            obj(Items.BRONZE_MACE, quantity = 1, slots = 8)
            obj(Items.BRONZE_BATTLEAXE, quantity = 1, slots = 1)
            obj(Items.AIR_RUNE, quantityRange = 1..5, slots = 2)
            obj(Items.MIND_RUNE, quantityRange = 1..3, slots = 1)
            obj(Items.SHORTBOW, quantity = 1, slots = 2)
            obj(Items.BRONZE_ARROW, quantityRange = 3..12, slots = 8)
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
            respawnDelay = 35
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 5
            attack = 1
            strength = 1
            defence = 1
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = -15
            defenceSlash = -15
            defenceCrush = -15
            defenceMagic = -15
            defenceRanged = -15
        }


        anims {
            attack = Animation.GOBLIN_PUNCH
            block = Animation.GOBLIN_DEFEND
            death = Animation.GOBLIN_DEATH
        }

        sound {
            attackSound = Sound.GOBLIN_ATTACK
            blockSound = Sound.GOBLIN_HIT
            deathSound = Sound.GOBLIN_DEATH
        }
    }
}