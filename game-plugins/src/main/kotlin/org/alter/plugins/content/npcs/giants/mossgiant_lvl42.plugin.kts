package org.alter.plugins.content.npcs.giants;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.MOSS_GIANT,
    Npcs.MOSS_GIANT_2091,
    Npcs.MOSS_GIANT_2092,
    Npcs.MOSS_GIANT_2093
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
        }
        table("main") {
            total(128)
            obj(Items.AIR_RUNE, quantityRange = 25..150, 18)
            obj(Items.WATER_RUNE, quantityRange = 10..125, 18)
            obj(Items.EARTH_RUNE, quantityRange = 10..100, 18)
            obj(Items.FIRE_RUNE, quantityRange = 10..95, 18)
            obj(Items.MIND_RUNE, quantityRange = 10..75, 18)
            obj(Items.CHAOS_RUNE, quantityRange = 10..50, 15)
            obj(Items.DEATH_RUNE, quantityRange = 1..25, 9)
            obj(Items.COINS, quantityRange = 100..250, 60)
            nothing(20)
        }
        table("rare") {
            total(128)
            obj(Items.UNCUT_DIAMOND, quantity = 1, 2)
            obj(Items.UNCUT_EMERALD, quantity = 1, 7)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, 16)
            obj(Items.ADAMANT_2H_SWORD, quantity = 1, 5)
            obj(Items.ADAMANT_SCIMITAR, quantity = 1, 5)
            obj(Items.ADAMANT_BATTLEAXE, quantity = 1, 5)
            obj(Items.COAL, quantityRange = 3..25, 3)
            obj(Items.ADAMANT_PICKAXE, quantity = 1, 3)
            obj(Items.ADAMANT_AXE, quantity = 1, 3)
            nothing(80)
        }
        table("second") {
            total(128)
            obj(Items.RUNE_ESSENCE, quantityRange = 1..55, 3)
            obj(Items.IRON_ARROW, quantityRange = 50..150, 10)
            obj(Items.STEEL_ARROW, quantityRange = 1..125, 14)
            obj(Items.UNCUT_RUBY, quantity = 1, 5)
            nothing(96)
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
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 60
            attack = 30
            strength = 30
            defence = 30
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 0
            defenceSlash = 0
            defenceCrush = 0
            defenceMagic = 0
            defenceRanged = 0
        }


        anims {
            attack = Animation.MOSS_GIANT_ATTACK
            block = Animation.MOSS_GIANT_HIT
            death = Animation.MOSS_GIANT_DEATH
        }

        sound {
            attackSound = Sound.MOSS_GIANT_ATTACK
            blockSound = Sound.GIANT_HIT
            deathSound = Sound.GIANT_DEATH
        }
    }
}