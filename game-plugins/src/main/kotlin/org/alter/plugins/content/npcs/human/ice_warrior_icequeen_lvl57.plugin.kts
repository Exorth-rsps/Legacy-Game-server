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
        table("main") {
            total(128)
            obj(Items.LIMPWURT_SEED, quantityRange = 1..3, 30)
            obj(Items.MARRENTILL_SEED, quantityRange = 1..3, 30)
            obj(Items.JANGERBERRY_SEED, quantityRange = 1..3, 30)
            obj(Items.TARROMIN_SEED, quantityRange = 1..3, 30)
            nothing(40)
        }
        table("second") {
            total(128)
            obj(Items.HARRALANDER_SEED, quantityRange = 1..2, 30)
            obj(Items.SNAPE_GRASS_SEED, quantityRange = 1..2, 30)
            obj(Items.RANARR_SEED, quantityRange = 1..2, 30)
            obj(Items.WHITEBERRY_SEED, quantityRange = 1..2, 30)
            nothing(40)
        }
        table("herbs") {
            total(128)
            obj(Items.TOADFLAX_SEED, quantityRange = 1..2, 30)
            obj(Items.IRIT_SEED, quantityRange = 1..2, 30)
            obj(Items.POISON_IVY_SEED, quantityRange = 1..2, 30)
            obj(Items.AVANTOE_SEED, quantityRange = 1..2, 30)
            obj(Items.KWUARM_SEED, quantityRange = 1..2, 30)
            nothing(40)
        }
        table("gems") {
            total(128)
            obj(Items.SNAPDRAGON_SEED, quantityRange = 1..2, 30)
            obj(Items.CADANTINE_SEED, quantityRange = 1..2, 30)
            obj(Items.LANTADYME_SEED, quantityRange = 1..2, 30)
            nothing(60)
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

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.ICE_WARRIOR_HIT
            deathSound = Sound.ICE_WARRIOR_DEATH
        }
    }
}