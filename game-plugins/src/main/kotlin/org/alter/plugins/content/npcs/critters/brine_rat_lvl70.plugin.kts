package org.alter.plugins.content.npcs.critters;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.GIANT_RAT_2858
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
            obj(Items.RAW_RAT_MEAT, quantity = 1)
        }
        main {
            total(10240)
            obj(Items.CHAOS_RUNE, quantityRange = 1..40, slots = 40)
            obj(Items.DEATH_RUNE, quantityRange = 1..40, slots = 40)
            obj(Items.BLOOD_RUNE, quantityRange = 1..20, slots = 20)
            obj(Items.WRATH_RUNE, quantityRange = 1..10, slots = 2)
            obj(Items.MITHRIL_ARROW, quantityRange = 50..75, slots = 20)
            obj(Items.ADAMANT_ARROW, quantityRange = 1..50, slots = 2)
            obj(Items.RAW_LOBSTER_NOTED, quantityRange = 1..10, slots = 20)
            obj(Items.RAW_SWORDFISH_NOTED, quantityRange = 1..9, slots = 20)
            obj(Items.RAW_RAT_MEAT_NOTED, quantityRange = 1..18, slots = 40)
            obj(Items.RAW_TUNA_NOTED, quantityRange = 1..12, slots = 40)
            obj(Items.RAW_SHARK_NOTED, quantityRange = 1..3, slots = 2)
            obj(Items.COINS, quantityRange = 150..950, slots = 80)
            obj(Items.BRINE_SABRE, quantity = 1, slots = 1)
            nothing(1280)
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
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 50
            attack = 70
            strength = 79
            defence = 40
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
            attack = Animation.BRINE_RAT_ATTACK
            block = Animation.BRINE_RAT_HIT
            death = Animation.BRINE_RAT_DEATH
        }

        sound {
            attackSound = Sound.RAT_ATTACK
            blockSound = Sound.RAT_HIT
            deathSound = Sound.RAT_DEATH
        }
    }
}