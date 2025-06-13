package org.alter.plugins.content.npcs.undead

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.FERAL_VAMPYRE_3237
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
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
            attackSpeed = 5
            respawnDelay = 40
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 55
            attack = 50
            strength = 48
            defence = 45
            magic = 1
            ranged = 1
        }

        anims {
            attack = Animation.VAMPIRE_ATTACK
            block = Animation.HUMAN_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.VAMPIRE_ATTACK
            blockSound = Sound.VAMPIRE_HIT
            deathSound = Sound.VAMPIRE_DEATH
        }
    }
}

