package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.THIEF_3252
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(512)
            nothing(slots = 64)
            obj(Items.COINS, quantityRange = 1..15, slots = 16)
            obj(Items.BRONZE_ARROW, quantityRange = 1..15, slots = 2)
            obj(Items.IRON_ARROW, quantityRange = 1..10, slots = 1)
            obj(Items.EARTH_RUNE, quantityRange = 1..10, slots = 4)
            obj(Items.FIRE_RUNE, quantityRange = 1..8, slots = 1)
            obj(Items.MIND_RUNE, quantityRange = 1..4, slots = 1)
        }
        table("Herbs") {
            total(256)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, slots = 2)
            obj(Items.GRIMY_HARRALANDER, quantity = 1, slots = 1)
            nothing(slots = 64)
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
            attackSpeed = 4
            respawnDelay = 25
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 17
            attack = 14
            strength = 13
            defence = 12
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
            attack = Animation.HUMAN_PUNCH
            block = Animation.HUMAN_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_BLOCK_1
            deathSound = Sound.HUMAN_DEATH
        }
    }
}