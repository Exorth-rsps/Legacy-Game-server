package org.alter.plugins.content.npcs.golems;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.RUNITE_GOLEM
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
        }
        table("main") {
            total(128)
            obj(Items.UNCUT_DIAMOND, quantity = 1, 4)
            obj(Items.UNCUT_RUBY, quantity = 1, 8)
            obj(Items.UNCUT_EMERALD, quantity = 1, 11)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, 20)
            obj(Items.BLOOD_RUNE, quantityRange = 1..5, 10)
            obj(Items.DEATH_RUNE, quantityRange = 1..5, 10)
            obj(Items.AIR_RUNE, quantityRange = 10..75, 15)
            obj(Items.WATER_RUNE, quantityRange = 10..75, 15)
            obj(Items.EARTH_RUNE, quantityRange = 10..75, 15)
            obj(Items.FIRE_RUNE, quantityRange = 10..75, 15)
            obj(Items.MIND_RUNE, quantityRange = 10..75, 15)
            nothing(20)
        }
        table("rare") {
            total(128)
            obj(Items.DRAGON_SCIMITAR, quantity = 1, 2)
            obj(Items.DRAGON_LONGSWORD, quantity = 1, 4)
            obj(Items.DRAGON_BATTLEAXE, quantity = 1, 4)
            obj(Items.DRAGON_SWORD, quantity = 1, 4)
            obj(Items.AMULET_OF_GLORY, quantity = 1, 1)
            obj(Items.AMULET_OF_FURY, quantity = 1, 1)
            nothing(112)
        }

    }



table.register(droptable, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> The Runite Golem has been slain by $playerName!", ChatMessageType.CONSOLE)
    }
}

on_npc_death(*ids) {
    table.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}


ids.forEach {
    set_combat_def(it) {
        configs {
            attackSpeed = 4
            respawnDelay = 300
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 270
            attack = 140
            strength = 150
            defence = 6165
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
            attack = Animation.ROCK_GOLEM_ATTACK
            block = Animation.ROCK_GOLEM_HIT
            death = Animation.ROCK_GOLEM_DEATH
        }

        sound {
            attackSound = Sound.GIANT_ATTACK
            blockSound = Sound.GIANT_HIT
            deathSound = Sound.GIANT_DEATH
        }
    }
}