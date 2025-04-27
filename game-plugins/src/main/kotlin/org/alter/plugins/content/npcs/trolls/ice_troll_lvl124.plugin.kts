package org.alter.plugins.content.npcs.trolls;

import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.ICE_TROLL
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.RAW_SHRIMPS_NOTED, quantityRange = 1..25, slots = 4)
            obj(Items.RAW_TROUT_NOTED, quantityRange = 1..25, slots = 4)
            obj(Items.RAW_TUNA_NOTED, quantityRange = 1..10, slots = 2)
            obj(Items.RAW_LOBSTER_NOTED, quantityRange = 1..5, slots = 2)
            obj(Items.RAW_SWORDFISH_NOTED, quantityRange = 1..3, slots = 1)
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
            hitpoints = 80
            attack = 140
            strength = 120
            defence = 80
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 30
            defenceSlash = 60
            defenceCrush = 30
            defenceMagic = 0
            defenceRanged = 0
        }

        anims {
            attack = Animation.TROLL_ATTACK
            block = Animation.TROLL_DEFEND
            death = Animation.TROLL_DEATH
        }

        sound {
            attackSound = Sound.TROLL_ATTACK
            blockSound = Sound.TROLL_HIT
            deathSound = Sound.TROLL_DEATH
        }
    }
}