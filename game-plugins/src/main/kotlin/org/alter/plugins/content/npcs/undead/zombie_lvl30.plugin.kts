package org.alter.plugins.content.npcs.undead;

import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.ZOMBIE_2501
)

val table = DropTableFactory
val droptable =
    table.build {
            guaranteed {
                obj(Items.BONES, quantity = 1)
            }
        main {
            total(512)
            nothing(64)
        }
        table("Herbs") {
            total(512)
            nothing(slots = 4)
        }
        table("Secondaries") {
            total(256)
            nothing(64)
        }
        table("gEMS") {
            total(256)
            nothing(64)
        }
        table("Herbs_Noted") {
            total(512)
            nothing(slots = 4)
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
            respawnDelay = 35
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 30
            attack = 25
            strength = 29
            defence = 20
            magic = 1
            ranged = 1
        }


        anims {
            attack = Animation.ZOMBIE_ATTACK
            block = Animation.ZOMBIE_HIT
            death = Animation.ZOMBIE_DEATH
        }

        sound {
            attackSound = Sound.ZOMBIE_ATTACK
            blockSound = Sound.ZOMBIE_HIT
            deathSound = Sound.ZOMBIE_DEATH
        }
    }
}