package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.CHAOS_DRUID
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        table("main") {
            total(128)
            obj(Items.GRIMY_GUAM_LEAF_NOTED, quantityRange = 1..3, 20)
            obj(Items.GRIMY_MARRENTILL_NOTED, quantityRange = 1..3, 20)
            obj(Items.GRIMY_TARROMIN_NOTED, quantityRange = 1..3, 16)
            obj(Items.GRIMY_RANARR_WEED_NOTED, quantityRange = 1..3, 16)
            obj(Items.GRIMY_AVANTOE_NOTED, quantityRange = 1..3, 12)
            obj(Items.GRIMY_IRIT_LEAF_NOTED, quantityRange = 1..3, 12)
            obj(Items.GRIMY_KWUARM_NOTED, quantityRange = 1..3, 8)
            nothing(20)
        }
        table("second") {
            total(128)
            obj(Items.GRIMY_CADANTINE_NOTED, quantityRange = 1..2, 8)
            obj(Items.GRIMY_DWARF_WEED_NOTED, quantityRange = 1..2, 4)
            obj(Items.GRIMY_TORSTOL_NOTED, quantityRange = 1..2, 4)
            nothing(112)
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
            respawnDelay = 25
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 20
            attack = 8
            strength = 8
            defence = 12
            magic = 10
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