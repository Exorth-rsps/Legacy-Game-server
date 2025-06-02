package org.alter.plugins.content.npcs.animals;

import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.GORAK
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
            attackSpeed = 4
            respawnDelay = 50
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 112
            attack = 130
            strength = 130
            defence = 131
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 10
            defenceSlash = 10
            defenceCrush = 0
            defenceMagic = 0
            defenceRanged = 10
        }

        aggro {
            radius = 5
            searchDelay = 2
            aggroTimer = 100
        }

        anims {
            attack = Animation.GORAK_ATTACK
            block = Animation.GORAK_DEFEND
            death = Animation.GORAK_DEATH
        }

        sound {
            attackSound = Sound.GORAK_ATTACK
            blockSound = Sound.GORAK_HIT
            deathSound = Sound.GORAK_DEATH
        }
    }
}