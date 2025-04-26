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
        main {
            total(1024)
            obj(Items.IRON_ARROW, quantityRange = 50..150, slots = 8)
            obj(Items.STEEL_ARROW, quantityRange = 1..125, slots = 4)
            obj(Items.AIR_RUNE, quantityRange = 25..150, slots = 8)
            obj(Items.WATER_RUNE, quantityRange = 10..125, slots = 8)
            obj(Items.EARTH_RUNE, quantityRange = 10..100, slots = 8)
            obj(Items.FIRE_RUNE, quantityRange = 10..95, slots = 8)
            obj(Items.MIND_RUNE, quantityRange = 10..75, slots = 8)
            obj(Items.CHAOS_RUNE, quantityRange = 10..50, slots = 4)
            obj(Items.DEATH_RUNE, quantityRange = 1..25, slots = 2)
            obj(Items.COINS, quantityRange = 100..250, slots = 4)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, slots = 4)
            obj(Items.UNCUT_EMERALD, quantity = 1, slots = 2)
            obj(Items.UNCUT_RUBY, quantity = 1, slots = 2)
            obj(Items.UNCUT_DIAMOND, quantity = 1, slots = 2)
            obj(Items.ADAMANT_AXE, quantity = 1, slots = 1)
            obj(Items.ADAMANT_PICKAXE, quantity = 1, slots = 1)
            obj(Items.ADAMANT_2H_SWORD, quantity = 1, slots = 1)
            obj(Items.ADAMANT_BATTLEAXE, quantity = 1, slots = 1)
            obj(Items.ADAMANT_SCIMITAR, quantity = 1, slots = 1)
            obj(Items.COAL_NOTED, quantityRange = 3..25, slots = 1)
            obj(Items.RUNE_ESSENCE_NOTED, quantityRange = 1..55, slots = 1)
            nothing(slots = 128)
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