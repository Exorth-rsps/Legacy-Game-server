package org.alter.plugins.content.npcs.other

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.GIANT_ROC
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
        }
        main {
            total(1024)
            obj(Items.LONG_BONE, quantity = 1, slots = 1)
            obj(Items.CURVED_BONE, quantity = 1, slots = 1)
            nothing(128)
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
            respawnDelay = 200
            poisonChance = 0.0
            venomChance = 0.0
            followRange = 10
        }
        stats {
            hitpoints = 200
            attack = 150
            strength = 150
            defence = 150
            magic = 1
            ranged = 150
        }

        bonuses {
            defenceStab = 50
            defenceSlash = 50
            defenceCrush = 50
            defenceMagic = 0
            defenceRanged = 50
        }

        anims {
            attack = Animation.BIRD_ATTACK
            block = Animation.BIRD_HIT
            death = Animation.BIRD_DEATH
        }

        sound {
            attackSound = Sound.GIANT_ROC_PECK_ATTACK
            blockSound = Sound.GIANT_ROC_HIT
            deathSound = Sound.GIANT_ROC_DEATH
        }
        aggro {
            radius = 8
        }
    }
}
