package org.alter.plugins.content.npcs.other;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.HOBGOBLIN
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.IRON_SWORD, quantity = 1, slots = 16)
            obj(Items.STEEL_DAGGER, quantity = 1, slots = 4)
            obj(Items.STEEL_LONGSWORD, quantity = 1, slots = 4)
            obj(Items.AIR_RUNE, quantityRange = 1..25, slots = 4)
            obj(Items.WATER_RUNE, quantityRange = 1..25, slots = 4)
            obj(Items.EARTH_RUNE, quantityRange = 1..25, slots = 4)
            obj(Items.FIRE_RUNE, quantityRange = 1..25, slots = 4)
            obj(Items.CHAOS_RUNE, quantityRange = 1..10, slots = 2)
            obj(Items.IRON_ARROW, quantityRange = 25..50, slots = 2)
            obj(Items.STEEL_ARROW, quantityRange = 10..25, slots = 2)
            obj(Items.MITHRIL_ARROWTIPS, quantityRange = 1..50, slots = 1)
            obj(Items.COINS, quantityRange = 25..150, slots = 16)
            obj(Items.GOBLIN_MAIL, quantity = 1, slots = 16)
            obj(Items.LIMPWURT_ROOT, quantity = 1, slots = 2)
            nothing(16)
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
            hitpoints = 68
            attack = 44
            strength = 48
            defence = 48
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
            attack = Animation.HOBGOBLIN_ATTACK
            block = Animation.HOBGOBLIN_HIT
            death = Animation.HOBGOBLIN_DEATH
        }

        sound {
            attackSound = Sound.GOBLIN_ATTACK
            blockSound = Sound.GOBLIN_HIT
            deathSound = Sound.GOBLIN_DEATH
        }
    }
}