package org.alter.plugins.content.npcs.dragons.kbd

import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.drops.DropTableFactory

set_multi_combat_region(region = 9033)

spawn_npc(Npcs.KING_BLACK_DRAGON, x = 2274, z = 4698, walkRadius = 5)

val ids = intArrayOf(
    Npcs.KING_BLACK_DRAGON,
    Npcs.KING_BLACK_DRAGON_2642,
    Npcs.KING_BLACK_DRAGON_6502
)
val table = DropTableFactory
val kbd =
    table.build {
        guaranteed {
            obj(Items.DRAGON_BONES, quantity = 1)
            obj(Items.BLACK_DRAGONHIDE, quantity = 1)
        }
        table("main") {
            total(128)
            obj(Items.RUNE_LONGSWORD, quantity = 1, 30)
            obj(Items.AIR_RUNE, quantityRange = 100..300, 20)
            obj(Items.FIRE_RUNE, quantityRange = 100..300, 20)
            obj(Items.BLOOD_RUNE, quantityRange = 10..50, 20)
            obj(Items.CHAOS_RUNE, quantityRange = 50..150, 20)
            obj(Items.DEATH_RUNE, quantityRange = 30..100, 20)
            nothing (40)
        }
        table("second") {
            total(128)
            obj(Items.YEW_LOGS_NOTED, quantityRange = 1..150, 20)
            obj(Items.ADAMANTITE_BAR_NOTED, quantityRange = 1..3, 20)
            obj(Items.SHARK, quantityRange = 1..5, 20)
            obj(Items.ADAMANT_PLATELEGS, quantity = 1, 15)
            obj(Items.ADAMANT_PLATEBODY, quantity = 1, 15)
            obj(Items.ADAMANT_KITESHIELD, quantity = 1, 15)
            obj(Items.ADAMANT_FULL_HELM, quantity = 1, 15)
            obj(Items.RUNITE_BAR, quantity = 1, 15)
            nothing(40)
        }
        table("rare") {
            total(128)
            obj(Items.DRAGON_MED_HELM, quantity = 1, 10)
            obj(Items.DRAGON_PLATELEGS, quantity = 1, 8)
            obj(Items.DRAGON_CHAINBODY_3140, quantity = 1, 8)
            obj(Items.DRAGON_PLATESKIRT, quantity = 1, 8)
            obj(Items.DRAGON_BOOTS, quantity = 1, 8)
            obj(Items.DRAGON_ARROWTIPS, quantityRange = 1..50, 8)
            obj(Items.DRAGON_PLATEBODY, quantity = 1, 3)
            obj(Items.DRAGON_FULL_HELM, quantity = 1, 3)
            obj(Items.DRAGON_AXE, quantity = 1, 3)
            obj(Items.DRAGON_PICKAXE, quantity = 1, 3)
            obj(Items.DRAGONFIRE_SHIELD, quantity = 1, 3)
            nothing(60)
        }


    }

table.register(kbd, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> The King Black Dragon has been slain by $playerName!", ChatMessageType.CONSOLE)
    }
}
on_npc_death(*ids) {
    table.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}
ids.forEach {
    set_combat_def(it) {
        species {
            + NpcSpecies.DRAGON
            + NpcSpecies.BASIC_DRAGON
        }
        configs {
            attackSpeed = 4
            respawnDelay = 300
            poisonChance = 0.0
            venomChance = 0.0
            followRange = 50
        }
        stats {
            hitpoints = 240
            attack = 240
            strength = 240
            defence = 240
            magic = 240
            ranged = 1
        }

        bonuses {
            defenceStab = 60
            defenceSlash = 90
            defenceCrush = 90
            defenceMagic = 80
            defenceRanged = 70
        }

        anims {
            attack = Animation.CHROMATIC_DRAGON_HIT
            block = Animation.CHROMATIC_DRAGON_HIT
            death = Animation.CHROMATIC_DRAGON_DEATH
        }

        sound {
            attackSound = Sound.DRAGON_ATTACK
            blockSound = Sound.DRAGON_HIT
            deathSound = Sound.DRAGON_DEATH
        }
        aggro {
            radius = 16
            searchDelay = 1
        }

    }
}
