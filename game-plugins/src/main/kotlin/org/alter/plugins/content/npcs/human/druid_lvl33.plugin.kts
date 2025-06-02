package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.DRUID
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
        table("Herbs") {
            total(512)
            nothing(slots = 4)
        }
        table("Herbs_Noted") {
            total(256)
            nothing(64)
        }
        table("Coins") {
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
            respawnDelay = 25
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 30
            attack = 28
            strength = 28
            defence = 32
            magic = 25
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