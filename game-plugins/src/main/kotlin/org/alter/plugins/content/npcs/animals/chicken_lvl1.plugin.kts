package org.alter.plugins.content.npcs.animals;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.CHICKEN_1173,
    Npcs.CHICKEN_1174
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
            obj(Items.RAW_CHICKEN, quantity = 1)
        }
        main {
            total(256)
            nothing(slots = 32)
            obj(Items.FEATHER, quantityRange = 1..5, slots = 8)
            obj(Items.FEATHER, quantityRange = 5..15, slots = 2)
            obj(Items.FEATHER, quantityRange = 12..25, slots = 1)
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
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 3
            attack = 1
            strength = 1
            defence = 1
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = -42
            defenceSlash = -42
            defenceCrush = -42
            defenceMagic = -42
            defenceRanged = -42
        }


        anims {
            attack = Animation.CHICKEN_ATTACK
            block = Animation.CHICKEN_DEFEND
            death = Animation.CHICKEN_DEATH
        }

        sound {
            attackSound = Sound.CHICKEN_ATTACK
            blockSound = Sound.CHICKEN_HIT
            deathSound = Sound.CHICKEN_DEATH
        }
    }
}