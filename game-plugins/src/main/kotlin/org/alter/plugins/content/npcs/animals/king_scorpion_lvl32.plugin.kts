package org.alter.plugins.content.npcs.animals;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.KING_SCORPION
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
        }
        main {
            total(512)
            obj(Items.COPPER_ORE_NOTED, quantityRange = 3..10, slots = 2)
            obj(Items.TIN_ORE_NOTED, quantityRange = 3..10, slots = 2)
            obj(Items.IRON_ORE_NOTED, quantityRange = 2..5, slots = 2)
            obj(Items.MUDDY_KEY, quantity = 1, slots = 1)
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
            hitpoints = 30
            attack = 30
            strength = 29
            defence = 23
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 5
            defenceSlash = 15
            defenceCrush = 15
            defenceMagic = 0
            defenceRanged = 5
        }

        anims {
            attack = Animation.SCORPION_ATTACK
            block = Animation.SCORPION_HIT
            death = Animation.SCORPION_DEATH
        }

        sound {
            attackSound = Sound.SCORPION_ATTACK
            blockSound = Sound.SCORPION_HIT
            deathSound = Sound.SCORPION_DEATH
        }
    }
}