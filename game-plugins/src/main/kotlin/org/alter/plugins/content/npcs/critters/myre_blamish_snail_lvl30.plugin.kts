package org.alter.plugins.content.npcs.critters

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.MYRE_BLAMISH_SNAIL
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BLAMISH_MYRE_SHELL, quantity = 1)
        }
        main {
            total(128)
            nothing(16)
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
            hitpoints = 25
            attack = 18
            strength = 18
            defence = 18
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 5
            defenceSlash = 5
            defenceCrush = 5
            defenceMagic = 5
            defenceRanged = 5
        }

        anims {
            attack = Animation.ROCKSLUG_ATTACK
            block = Animation.ROCKSLUG_HIT
            death = Animation.ROCKSLUG_DEATH
        }

        sound {
            attackSound = Sound.SNAIL_SPIT
            blockSound = Sound.SNAIL_HIT
            deathSound = Sound.SNAIL_DEATH
        }
    }
}

