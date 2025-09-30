package org.alter.plugins.content.npcs.undead

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.FERAL_VAMPYRE_3237
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.COINS, quantity = 15)
        }
        table("herbs") {
            total(128)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, 60)
            obj(Items.GRIMY_MARRENTILL, quantity = 1, 52)
            obj(Items.GRIMY_TARROMIN, quantity = 1, 48)
            nothing(80)
        }
        table("herb-secondaries") {
            total(128)
            obj(Items.RANARR_SEED, quantity = 1, 39)
            obj(Items.TARROMIN_SEED, quantity = 1, 42)
            obj(Items.KWUARM_SEED, quantity = 1, 33)
            nothing(80)
        }
        table("herbs-noted") {
            total(128)
            obj(Items.GRIMY_HARRALANDER, quantity = 1, 44)
            obj(Items.GRIMY_RANARR_WEED, quantity = 1, 37)
            obj(Items.GRIMY_CADANTINE, quantity = 1, 31)
            nothing(80)
        }
        table("second") {
            total(128)
            obj(Items.EARTH_TALISMAN, quantity = 1, 22)
            obj(Items.UNCUT_DIAMOND, quantity = 1, 12)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, 21)
            obj(Items.UNCUT_EMERALD, quantity = 1, 18)
            obj(Items.UNCUT_RUBY, quantity = 1, 14)
            obj(Items.CHAOS_TALISMAN, quantity = 1, 17)
            obj(Items.NATURE_TALISMAN, quantity = 1, 20)
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
            attackSpeed = 5
            respawnDelay = 40
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 55
            attack = 50
            strength = 48
            defence = 45
            magic = 1
            ranged = 1
        }

        anims {
            attack = Animation.VAMPIRE_ATTACK
            block = Animation.HUMAN_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.VAMPIRE_ATTACK
            blockSound = Sound.VAMPIRE_HIT
            deathSound = Sound.VAMPIRE_DEATH
        }
    }
}

