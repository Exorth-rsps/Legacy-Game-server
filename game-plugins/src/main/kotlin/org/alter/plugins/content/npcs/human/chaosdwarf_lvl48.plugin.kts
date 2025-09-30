package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.CHAOS_DWARF
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        table("coins") {
            total(128)
            obj(Items.COINS, quantityRange = 1..50, 80)
            nothing(48)
        }
        table("main") {
            total(128)
            obj(Items.AIR_RUNE, quantityRange = 1..20, 30)
            obj(Items.MIND_RUNE, quantityRange = 1..20, 30)
            obj(Items.CHAOS_RUNE, quantityRange = 1..10, 20)
            obj(Items.DEATH_RUNE, quantityRange = 1..5, 14)
            obj(Items.WATER_RUNE, quantityRange = 1..20, 20)
            obj(Items.MITHRIL_BAR, quantity = 1, 2)
            obj(Items.COAL, quantityRange = 1..3, 10)
            nothing(20)
        }
        table("gems") {
            total(128)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, 20)
            obj(Items.UNCUT_EMERALD, quantity = 1, 17)
            nothing(91)
        }
        table("second") {
            total(128)
            obj(Items.MITHRIL_MED_HELM, quantity = 1, 17)
            obj(Items.MITHRIL_SQ_SHIELD, quantity = 1, 17)
            obj(Items.MITHRIL_CHAINBODY, quantity = 1, 17)
            obj(Items.MITHRIL_SWORD, quantity = 1, 17)
            obj(Items.MITHRIL_LONGSWORD, quantity = 1, 17)
            obj(Items.MITHRIL_KITESHIELD, quantity = 1, 17)
            obj(Items.MITHRIL_FULL_HELM, quantity = 1, 10)
            obj(Items.MITHRIL_PLATELEGS, quantity = 1, 10)
            obj(Items.MITHRIL_PLATEBODY, quantity = 1, 10)
            nothing(20)
        }
        table("rare") {
            total(128)
            obj(Items.MITHRIL_PLATESKIRT, quantity = 1, 10)
            obj(Items.MITHRIL_WARHAMMER, quantity = 1, 6)
            obj(Items.MITHRIL_BATTLEAXE, quantity = 1, 6)
            obj(Items.MITHRIL_SCIMITAR, quantity = 1, 6)
            nothing(100)
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
            respawnDelay = 60
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 61
            attack = 38
            strength = 42
            defence = 28
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 40
            defenceSlash = 34
            defenceCrush = 25
            defenceMagic = 10
            defenceRanged = 35
        }


        anims {
            attack = Animation.DWARF_ATTACK
            block = Animation.DWARF_HIT
            death = Animation.DWARF_DEATH
        }

        sound {
            attackSound = Sound.DWARF_ATTACK
            blockSound = Sound.DWARF_HIT
            deathSound = Sound.DWARF_DEATH
        }
    }
}