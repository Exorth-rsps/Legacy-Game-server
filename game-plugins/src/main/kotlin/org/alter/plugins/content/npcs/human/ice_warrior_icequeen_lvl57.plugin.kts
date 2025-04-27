package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.ICE_WARRIOR_2851
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.ARROW_SHAFT, quantity = 1, slots = 4)
            obj(Items.FEATHER, quantity = 1, slots = 4)
            obj(Items.BRONZE_ARROWTIPS, quantity = 1, slots = 2)
            obj(Items.IRON_ARROWTIPS, quantity = 1, slots = 2)
            obj(Items.STEEL_ARROWTIPS, quantity = 1, slots = 1)
            obj(Items.MITHRIL_ARROWTIPS, quantity = 1, slots = 1)
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
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 59
            attack = 47
            strength = 47
            defence = 47
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 30
            defenceSlash = 40
            defenceCrush = 20
            defenceMagic = 10
            defenceRanged = 30
        }


        anims {
            attack = Animation.ICE_WARRIOR_ATTACK
            block = Animation.ICE_WARRIOR_HIT
            death = Animation.HUMAN_DEATH
        }
        aggro {
            radius = 10
            searchDelay = 1
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.ICE_WARRIOR_HIT
            deathSound = Sound.ICE_WARRIOR_DEATH
        }
    }
}