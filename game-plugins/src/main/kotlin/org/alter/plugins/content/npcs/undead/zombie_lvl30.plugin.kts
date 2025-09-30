package org.alter.plugins.content.npcs.undead;

import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.ZOMBIE_2501
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        table("gems") {
            total(128)
            obj(Items.UNCUT_DIAMOND, quantity = 1, 3)
            obj(Items.UNCUT_RUBY, quantity = 1, 5)
            obj(Items.UNCUT_EMERALD, quantity = 1, 8)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, 12)
            obj(Items.TIN_ORE, quantity = 1, 6)
            obj(Items.EYE_OF_NEWT, quantity = 1, 6)
            nothing(20)
        }
        table("coins") {
            total(128)
            obj(Items.COINS, quantity = 10, 40)
            obj(Items.COINS, quantity = 18, 30)
            obj(Items.COINS, quantity = 26, 25)
            obj(Items.COINS, quantity = 35, 20)
            obj(Items.COINS, quantity = 1, 2)
            nothing(50)
        }
        table("herbs") {
            total(128)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, 15)
            obj(Items.GRIMY_MARRENTILL, quantity = 1, 15)
            obj(Items.GRIMY_TARROMIN, quantity = 1, 15)
            obj(Items.GRIMY_HARRALANDER, quantity = 1, 15)
            obj(Items.GRIMY_RANARR_WEED, quantity = 1, 15)
            obj(Items.GRIMY_IRIT_LEAF, quantity = 1, 15)
            obj(Items.GRIMY_AVANTOE, quantity = 1, 15)
            obj(Items.GRIMY_KWUARM, quantity = 1, 15)
            obj(Items.GRIMY_CADANTINE, quantity = 1, 15)
            obj(Items.GRIMY_DWARF_WEED, quantity = 1, 15)
            nothing(20)
        }
        table("main") {
            total(128)
            obj(Items.IRON_MACE, quantity = 1, 15)
            obj(Items.IRON_DAGGER, quantity = 1, 15)
            obj(Items.BRONZE_KITESHIELD, quantity = 1, 15)
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
            attackSpeed = 5
            respawnDelay = 35
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 30
            attack = 25
            strength = 29
            defence = 20
            magic = 1
            ranged = 1
        }


        anims {
            attack = Animation.ZOMBIE_ATTACK
            block = Animation.ZOMBIE_HIT
            death = Animation.ZOMBIE_DEATH
        }

        sound {
            attackSound = Sound.ZOMBIE_ATTACK
            blockSound = Sound.ZOMBIE_HIT
            deathSound = Sound.ZOMBIE_DEATH
        }
    }
}