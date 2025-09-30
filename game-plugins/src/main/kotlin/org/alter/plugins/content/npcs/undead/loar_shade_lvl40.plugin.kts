package org.alter.plugins.content.npcs.undead;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.LOAR_SHADOW,
    Npcs.LOAR_SHADE
)

on_npc_combat(Npcs.LOAR_SHADOW) {
    if (npc.getTransmogId() != Npcs.LOAR_SHADE) {
        npc.setTransmogId(Npcs.LOAR_SHADE)
    }
    world.plugins.executeCombat(npc)
}

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.COINS, quantity = 2500)
            obj(Items.BONES, quantity = 1)
        }
        table("coins") {
            total(128)
            obj(Items.COINS, quantityRange = 1100..3500, 2)
            nothing(20)
        }
        table("main") {
            total(128)
            obj(Items.GRIMY_GUAM_LEAF, quantityRange = 1..9, 20)
            obj(Items.GRIMY_MARRENTILL, quantityRange = 1..9, 18)
            obj(Items.GRIMY_TARROMIN, quantityRange = 1..9, 19)
            obj(Items.GRIMY_RANARR_WEED, quantityRange = 1..9, 17)
            nothing(20)
        }
        table("second") {
            total(128)
            obj(Items.GRIMY_IRIT_LEAF, quantityRange = 1..5, 15)
            obj(Items.GRIMY_KWUARM, quantityRange = 1..5, 14)
            obj(Items.GRIMY_SNAPDRAGON, quantityRange = 1..5, 13)
            obj(Items.GRIMY_CADANTINE, quantityRange = 1..5, 12)
            obj(Items.GRIMY_DWARF_WEED, quantityRange = 1..5, 11)
            obj(Items.GRIMY_TORSTOL_NOTED, quantityRange = 1..5, 10)
            obj(Items.GRIMY_HARRALANDER, quantityRange = 1..5, 21)
            obj(Items.GRIMY_LANTADYME, quantityRange = 1..4, 22)
            obj(Items.GRIMY_TOADFLAX, quantityRange = 1..3, 24)
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
            respawnDelay = 35
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 38
            attack = 45
            strength = 30
            defence = 26
            magic = 1
            ranged = 1
        }


        anims {
            attack = Animation.SHADE_ATTACK
            block = Animation.SHADE_HIT
            death = Animation.SHADE_DEATH
        }

        sound {
            attackSound = Sound.SHADE_ATTACK
            blockSound = Sound.SHADE_HIT
            deathSound = Sound.SHADE_DEATH
        }
    }
}