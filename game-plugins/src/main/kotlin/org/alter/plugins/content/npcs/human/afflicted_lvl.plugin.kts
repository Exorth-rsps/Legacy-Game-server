package org.alter.plugins.content.npcs.human

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.AFFLICTED,
    Npcs.AFFLICTED_1294,
    Npcs.AFFLICTED_1297,
    Npcs.AFFLICTED_1298
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(64)
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
            respawnDelay = 20
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 20
            attack = 15
            strength = 15
            defence = 10
            magic = 1
            ranged = 1
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

