package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory
set_multi_combat_region(region = 11829)
val ids = intArrayOf(
    Npcs.KAMIL
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        table("coins") {
            total(128)
            obj(Items.COINS, quantityRange = 250..1500, 90)
            nothing(60)
        }
        table("main") {
            total(128)
            obj(Items.AIR_RUNE, quantityRange = 1..100, 10)
            obj(Items.MIND_RUNE, quantityRange = 1..100, 10)
            obj(Items.WATER_RUNE, quantityRange = 1..50, 10)
            obj(Items.EARTH_RUNE, quantityRange = 1..50, 10)
            obj(Items.FIRE_RUNE, quantityRange = 1..50, 10)
            obj(Items.CHAOS_RUNE, quantityRange = 1..40, 4)
            obj(Items.DEATH_RUNE, quantityRange = 1..30, 4)
            obj(Items.BLOOD_RUNE, quantityRange = 1..25, 5)
            obj(Items.WRATH_RUNE, quantityRange = 1..15, 3)
            obj(Items.BRONZE_ARROW, quantityRange = 50..100, 15)
            obj(Items.IRON_ARROW, quantityRange = 50..100, 15)
            obj(Items.STEEL_ARROW, quantityRange = 25..50, 15)
            obj(Items.MITHRIL_ARROW, quantityRange = 10..25, 15)
            obj(Items.ADAMANT_ARROW, quantityRange = 1..20, 10)
            nothing(20)
        }
        table("second") {
            total(128)
            obj(Items.RUNE_ARROW, quantityRange = 1..15, 8)
            obj(Items.WHITE_SWORD, quantity = 1, 15)
            obj(Items.WHITE_SCIMITAR, quantity = 1, 15)
            obj(Items.WHITE_BATTLEAXE, quantity = 1, 15)
            obj(Items.WHITE_2H_SWORD, quantity = 1, 10)
            nothing(20)
        }
        table("rare") {
            total(128)
            obj(Items.XERICIAN_HAT, quantity = 1, 2)
            obj(Items.XERICIAN_TOP, quantity = 1, 2)
            obj(Items.XERICIAN_ROBE, quantity = 1, 2)
            obj(Items.SNAKESKIN_BODY, quantity = 1, 9)
            obj(Items.SNAKESKIN_BOOTS, quantity = 1, 9)
            obj(Items.SNAKESKIN_CHAPS, quantity = 1, 9)
            obj(Items.SNAKESKIN_VAMBRACES, quantity = 1, 9)
            obj(Items.MAPLE_SHORTBOW, quantity = 1, 7)
            obj(Items.YEW_SHORTBOW, quantity = 1, 5)
            obj(Items.COLD_KEY, quantity = 1, 2)
            obj(Items.RUNE_MED_HELM, quantity = 1, 7)
            obj(Items.RUNE_SQ_SHIELD, quantity = 1, 7)
            obj(Items.RUNE_LONGSWORD, quantity = 1, 7)
            obj(Items.RUNE_SWORD, quantity = 1, 7)
            obj(Items.RUNE_MACE, quantity = 1, 7)
            nothing(60)
        }

    }



table.register(droptable, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> Kamil has been slain by $playerName!", ChatMessageType.CONSOLE)
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
            hitpoints = 255
            attack = 190
            strength = 80
            defence = 135
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 35
            defenceSlash = 60
            defenceCrush = 35
            defenceMagic = 0
            defenceRanged = 0
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