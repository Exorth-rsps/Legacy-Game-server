package org.alter.plugins.content.npcs.animals;

import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.COW,
    Npcs.COW_2791
)

val table = DropTableFactory
val cowCalf =
    table.build {
        guaranteed {
            obj(Items.BONES)
            obj(Items.RAW_BEEF)
            obj(Items.COWHIDE)
        }
    }

table.register(cowCalf, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player

}

on_npc_death(*ids) {
    table.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}

val COW_YELL_DELAY = TimerKey()

ids.forEach { cow ->
    on_npc_spawn(npc = cow) {
        val npc = npc
        npc.timers[COW_YELL_DELAY] = world.random(100..200)
    }
}

on_timer(COW_YELL_DELAY) {
    val npc = npc
    if (!npc.isBeingAttacked()) {
        npc.forceChat("Moo")
    }
    npc.timers[COW_YELL_DELAY] = world.random(100..200)
}


ids.forEach {
    set_combat_def(it) {
        configs {
            attackSpeed = 6
            respawnDelay = 45
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 8
            attack = 1
            strength = 1
            defence = 1
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = -21
            defenceSlash = -21
            defenceCrush = -21
            defenceMagic = -21
            defenceRanged = -21
        }

        anims {
            attack = Animation.COW_ATTACK
            block = Animation.COW_HIT
            death = Animation.COW_DEATH
        }

        sound {
            attackSound = Sound.COW_ATTACK
            blockSound = Sound.COW_HIT
            deathSound = Sound.COW_DEATH
        }
    }
}