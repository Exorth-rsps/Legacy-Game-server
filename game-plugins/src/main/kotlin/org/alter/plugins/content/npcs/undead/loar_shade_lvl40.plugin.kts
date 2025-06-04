package org.alter.plugins.content.npcs.undead;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.LOAR_SHADOW,
    Npcs.LOAR_SHADE
)

on_npc_combat(Npcs.LOAR_SHADOW) {
    if (npc.getTransmogId() != Npcs.LOAR_SHADE) {
        npc.setTransmogId(Npcs.LOAR_SHADE)
    }
    world.plugins.executeCombat(npc)
}

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
            attackSpeed = 4
            respawnDelay = 35
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 38
            attack = 45
            strength = 30
            defence = 26
            magic = 1
            ranged = 1
        }


        anims {
            attack = Animation.SHADE_ATTACK
            block = Animation.SHADE_HIT
            death = Animation.SHADE_DEATH
        }

        sound {
            attackSound = Sound.SHADE_ATTACK
            blockSound = Sound.SHADE_HIT
            deathSound = Sound.SHADE_DEATH
        }
    }
}