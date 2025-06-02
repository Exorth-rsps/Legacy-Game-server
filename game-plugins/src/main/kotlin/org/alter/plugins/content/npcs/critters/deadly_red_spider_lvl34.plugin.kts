package org.alter.plugins.content.npcs.critters;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.DEADLY_RED_SPIDER
)

val table = DropTableFactory
val droptable =
    table.build {
        main {
            total(128)
            nothing(16)
        }
        table("Herbs") {
            total(512)
            nothing(slots = 4)
        }
        table("Herbs_Noted") {
            total(256)
            nothing(64)
        }
        table("Secondaries") {
            total(256)
            nothing(64)
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
            hitpoints = 35
            attack = 30
            strength = 25
            defence = 30
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 15
            defenceSlash = 16
            defenceCrush = 7
            defenceMagic = 12
            defenceRanged = 16
        }
         aggro {
             radius = 5
             searchDelay = 1
             aggroTimer = 50
        }


        anims {
            attack = Animation.GIANT_SPIDER_ATTACK
            block = Animation.GIANT_SPIDER_DEFEND
            death = Animation.BRINE_RAT_DEATH
        }

        sound {
            attackSound = Sound.BIG_SPIDER_ATTACK
            blockSound = Sound.BIG_SPIDER_HIT
            deathSound = Sound.BIG_SPIDER_DEATH
        }
    }
}