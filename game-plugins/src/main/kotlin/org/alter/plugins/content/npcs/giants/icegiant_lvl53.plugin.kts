package org.alter.plugins.content.npcs.giants;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.ICE_GIANT
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.LOGS_NOTED, quantityRange = 1..50, slots = 4)
            obj(Items.OAK_LOGS_NOTED, quantityRange = 1..40, slots = 4)
            obj(Items.WILLOW_LOGS_NOTED, quantityRange = 1..30, slots = 2)
            obj(Items.MAPLE_LOGS_NOTED, quantityRange = 1..20, slots = 2)
            obj(Items.YEW_LOGS_NOTED, quantityRange = 1..10, slots = 1)
            obj(Items.FLAX_NOTED, quantityRange = 1..40, slots = 1)
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
            attackSpeed = 5
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 70
            attack = 40
            strength = 40
            defence = 40
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 0
            defenceSlash = 3
            defenceCrush = 2
            defenceMagic = 0
            defenceRanged = 0
        }


        anims {
            attack = Animation.ICE_GIANT_ATTACK
            block = Animation.ICE_GIANT_HIT
            death = Animation.ICE_GIANT_DEATH
        }

        sound {
            attackSound = Sound.GIANT_ATTACK
            blockSound = Sound.GIANT_HIT
            deathSound = Sound.GIANT_DEATH
        }
    }
}