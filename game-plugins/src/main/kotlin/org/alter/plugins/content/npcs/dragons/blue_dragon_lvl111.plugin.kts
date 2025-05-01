package org.alter.plugins.content.npcs.dragons

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.BLUE_DRAGON,
)
val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.DRAGON_BONES, quantity = 1)
            obj(Items.BLUE_DRAGONHIDE, quantity = 1)
        }
        main {
            total(512)
            obj(Items.STEEL_PLATELEGS, quantity = 1, slots = 2)
            obj(Items.STEEL_BATTLEAXE, quantity = 1, slots = 2)
            obj(Items.MITHRIL_AXE, quantity = 1, slots = 1)
            obj(Items.MITHRIL_KITESHIELD, quantity = 1, slots = 2)
            obj(Items.ADAMANT_FULL_HELM, quantity = 1, slots = 1)
            obj(Items.RUNE_DAGGER, quantity = 1, slots = 1)
            obj(Items.STEEL_BAR_NOTED, quantityRange = 1..5, slots = 2)
            obj(Items.WATER_RUNE, quantityRange = 1..9, slots = 16)
            obj(Items.FIRE_RUNE, quantityRange = 1..11, slots = 4)
            obj(Items.COINS_995, quantityRange = 66..621, slots = 2)
            obj(Items.BASS, quantity = 1, slots = 4)
            obj(Items.ADAMANTITE_ORE, quantity = 1, slots = 4)
            nothing(64)
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
        species {
            + NpcSpecies.DRAGON
            + NpcSpecies.BASIC_DRAGON
        }
        configs {
            attackSpeed = 4
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 105
            attack = 95
            strength = 95
            defence = 95
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 10
            defenceSlash = 70
            defenceCrush = 70
            defenceMagic = 60
            defenceRanged = 50
        }

        anims {
            attack = Animation.DRAGON_ATTACK
            block = Animation.DRAGON_HIT
            death = Animation.DRAGON_DEATH
        }

        sound {
            attackSound = Sound.DRAGON_ATTACK
            blockSound = Sound.DRAGON_HIT
            deathSound = Sound.DRAGON_DEATH
        }
        aggro {
            radius = 5
            searchDelay = 1
        }

    }
}
