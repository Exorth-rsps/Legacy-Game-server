package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.HERO_3295
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.COINS, quantity = 1, slots = 16)
            obj(Items.GOLD_ORE, quantity = 1, slots = 1)
            obj(Items.UNCUT_DIAMOND, quantity = 1, slots = 1)
            obj(Items.UNCUT_EMERALD, quantity = 1, slots = 1)
            nothing(64)
        }
        table("Runes") {
            total(512)
            obj(Items.AIR_RUNE, quantity = 1, slots = 16)
            obj(Items.WATER_RUNE, quantity = 1, slots = 16)
            obj(Items.EARTH_RUNE, quantity = 1, slots = 4)
            obj(Items.FIRE_RUNE, quantity = 1, slots = 4)
            obj(Items.DEATH_RUNE, quantity = 1, slots = 1)
            obj(Items.BLOOD_RUNE, quantity = 1, slots = 1)
            nothing(slots = 64)
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
            respawnDelay = 50
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 82
            attack = 54
            strength = 55
            defence = 54
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 87
            defenceSlash = 84
            defenceCrush = 76
            defenceMagic = -10
            defenceRanged = 79
        }


        anims {
            attack = Animation.HUMAN_SLASH_SWORD_ATTACK
            block = Animation.HUMAN_SHIELD_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_BLOCK_1
            deathSound = Sound.HUMAN_DEATH
        }
    }
}