package org.alter.plugins.content.npcs.undead

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.VYREWATCH_8256
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(256)
            nothing(32)
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
            respawnDelay = 40
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 80
            attack = 70
            strength = 70
            defence = 65
            magic = 60
            ranged = 60
        }

        anims {
            attack = Animation.VYREWATCH_ATTACK
            block = Animation.VYREWATCH_HIT
            death = Animation.VYREWATCH_DEATH
        }

        sound {
            attackSound = Sound.VYREWATCH_VAMPIRE_ATTACK
            blockSound = Sound.VYREWATCH_VAMPIRE_HIT
            deathSound = Sound.VYREWATCH_VAMPIRE_DEATH
        }
    }
}

