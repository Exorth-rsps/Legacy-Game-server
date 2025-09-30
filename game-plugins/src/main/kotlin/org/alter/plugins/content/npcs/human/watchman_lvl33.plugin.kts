package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.WATCHMAN_5420
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        table("rare") {
            total(128)
            obj(Items.AMULET_OF_ACCURACY, quantity = 1, 8)
            obj(Items.AMULET_OF_POWER, quantity = 1, 4)
            obj(Items.AMULET_OF_MAGIC, quantity = 1, 7)
            obj(Items.STEEL_SQ_SHIELD, quantity = 1, 14)
            obj(Items.STEEL_SWORD, quantity = 1, 14)
            obj(Items.STEEL_MED_HELM, quantity = 1, 14)
            obj(Items.STEEL_PLATELEGS, quantity = 1, 14)
            obj(Items.STEEL_CHAINBODY, quantity = 1, 14)
            obj(Items.UNCUT_RED_TOPAZ, quantity = 1, 20)
            nothing(20)
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
            respawnDelay = 20
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 22
            attack = 31
            strength = 31
            defence = 31
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 24
            defenceSlash = 14
            defenceCrush = 19
            defenceMagic = -4
            defenceRanged = 16
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