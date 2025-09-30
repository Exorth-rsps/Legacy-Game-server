package org.alter.plugins.content.npcs.human

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.AFFLICTED,
    Npcs.AFFLICTED_1294,
    Npcs.AFFLICTED_1297,
    Npcs.AFFLICTED_1298
)

val table = DropTableFactory
val droptable =
    table.build {
        table("herbs-noted") {
            total(128)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, 88)
            nothing(40)
        }
        guaranteed {
            obj(Items.COINS, quantityRange = 111..450)
            obj(Items.BONES, quantity = 1)
        }
        table("coins") {
            total(128)
            obj(Items.COINS, quantityRange = 100..500, 2)
            nothing(126)
        }
        table("main") {
            total(128)
            obj(Items.GRIMY_IRIT_LEAF, quantity = 1, 85)
            obj(Items.GRIMY_LANTADYME, quantity = 1, 80)
            nothing(80)
        }
        table("second") {
            total(128)
            obj(Items.GRIMY_HARRALANDER, quantity = 1, 75)
            obj(Items.GRIMY_CADANTINE, quantity = 1, 71)
            nothing(80)
        }
        table("herbs") {
            total(128)
            obj(Items.GRIMY_KWUARM, quantity = 1, 67)
            nothing(80)
        }
        table("rare") {
            total(128)
            obj(Items.GRIMY_AVANTOE, quantity = 1, 62)
            obj(Items.GRIMY_TARROMIN, quantity = 1, 77)
            nothing(80)
        }
        table("herb-secondaries") {
            total(128)
            obj(Items.GRIMY_MARRENTILL, quantity = 1, 81)
            nothing(80)
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
            hitpoints = 20
            attack = 15
            strength = 15
            defence = 10
            magic = 1
            ranged = 1
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

