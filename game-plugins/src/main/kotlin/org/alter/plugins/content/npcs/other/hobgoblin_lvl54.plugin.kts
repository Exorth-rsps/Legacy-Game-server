package org.alter.plugins.content.npcs.other;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.HOBGOBLIN
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.COINS, quantityRange = 1..50)
        }
        table("main") {
            total(128)
            obj(Items.BONES, quantity = 1, 70)
            obj(Items.GOBLIN_MAIL, quantity = 1, 30)
            obj(Items.IRON_SWORD, quantity = 1, 20)
            obj(Items.AIR_RUNE, quantityRange = 1..10, 10)
            nothing(30)
        }
        table("second") {
            total(128)
            obj(Items.STEEL_ARROW, quantityRange = 1..5, 10)
            nothing(118)
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