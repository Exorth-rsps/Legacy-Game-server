package org.alter.plugins.content.npcs.undead

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.VYREWATCH_8256
)

val table = DropTableFactory
val droptable =
    table.build {
        table("main") {
            total(128)
            obj(Items.ADAMANT_PLATEBODY, quantity = 1, 16)
            obj(Items.ADAMANT_PLATELEGS, quantity = 1, 17)
            obj(Items.RUNE_PLATELEGS, quantity = 1, 13)
            obj(Items.RUNE_FULL_HELM, quantity = 1, 12)
            nothing(20)
        }
        table("coins") {
            total(128)
            obj(Items.COINS, quantity = 3500, 3)
            nothing(20)
        }
        guaranteed {
            obj(Items.COINS, quantityRange = 2200..4500)
            obj(Items.BONES, quantityRange = 1..1)
        }
        table("second") {
            total(128)
            obj(Items.GUAM_SEED, quantityRange = 1..10, 14)
            obj(Items.MARRENTILL_SEED, quantityRange = 1..10, 15)
            obj(Items.TARROMIN_SEED, quantityRange = 1..10, 16)
            obj(Items.RANARR_SEED, quantityRange = 1..10, 17)
            obj(Items.IRIT_SEED, quantityRange = 1..6, 18)
            obj(Items.KWUARM_SEED, quantityRange = 1..6, 19)
            obj(Items.SNAPDRAGON_SEED, quantityRange = 1..6, 20)
            obj(Items.CADANTINE_SEED, quantityRange = 1..4, 21)
            nothing(20)
        }
        table("rare") {
            total(128)
            obj(Items.TORSTOL_SEED, quantityRange = 1..3, 23)
            obj(Items.DWARF_WEED_SEED, quantityRange = 1..4, 22)
            obj(Items.LANTADYME_SEED, quantityRange = 1..4, 24)
            obj(Items.TOADFLAX_SEED, quantityRange = 1..4, 25)
            obj(Items.HARRALANDER_SEED, quantityRange = 1..7, 26)
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
            respawnDelay = 40
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 80
            attack = 70
            strength = 70
            defence = 65
            magic = 60
            ranged = 60
        }

        anims {
            attack = Animation.VYREWATCH_ATTACK
            block = Animation.VYREWATCH_HIT
            death = Animation.VYREWATCH_DEATH
        }

        sound {
            attackSound = Sound.VYREWATCH_VAMPIRE_ATTACK
            blockSound = Sound.VYREWATCH_VAMPIRE_HIT
            deathSound = Sound.VYREWATCH_VAMPIRE_DEATH
        }
    }
}

