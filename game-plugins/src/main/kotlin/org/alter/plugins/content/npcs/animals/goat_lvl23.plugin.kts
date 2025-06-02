package org.alter.plugins.content.npcs.animals;

import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.GOAT
)

val table = DropTableFactory
val droptable =
    table.build {
            guaranteed {
                obj(Items.BONES, quantity = 1)
            }
        table("Herbs") {
            total(512)
            nothing(slots = 4)
        }
        table("Secondaries") {
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
            respawnDelay = 50
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 21
            attack = 20
            strength = 20
            defence = 20
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

        anims {
            attack = Animation.GOAT_ATTACK
            block = Animation.GOAT_HIT
            death = Animation.GOAT_DEATH
        }

        sound {
            attackSound = Sound.WOLF_ATTACK
            blockSound = Sound.WOLF_HIT
            deathSound = Sound.WOLF_DEATH
        }
    }
}