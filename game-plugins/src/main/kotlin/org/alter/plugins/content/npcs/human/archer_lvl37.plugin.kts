package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.ARCHER_3301
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.STEEL_ARROW, quantityRange = 5..100, slots = 4)
            obj(Items.IRON_ARROW, quantityRange = 5..200, slots = 4)
            obj(Items.SHORTBOW, quantity = 1, slots = 4)
            obj(Items.OAK_SHORTBOW, quantity = 1, slots = 4)
            obj(Items.STEEL_SWORD, quantity = 1, slots = 4)
            obj(Items.COINS_995, quantity = 25, slots = 16)
            nothing(slots = 64)
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
            attackSpeed = 6
            respawnDelay = 25
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 50
            attack = 20
            strength = 20
            defence = 20
            magic = 1
            ranged = 40
        }

        bonuses {
            defenceStab = 18
            defenceSlash = 23
            defenceCrush = 27
            defenceMagic = 10
            defenceRanged = 19
        }


        anims {
            attack = Animation.HUMAN_BOW_ATTACK
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