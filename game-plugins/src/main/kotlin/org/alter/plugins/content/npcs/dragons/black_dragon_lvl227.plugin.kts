package org.alter.plugins.content.npcs.dragons

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.RED_DRAGON,
)
val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.DRAGON_BONES, quantity = 1)
            obj(Items.BLACK_DRAGONHIDE, quantity = 1)
        }
        main {
            total(512)
            obj(Items.MITHRIL_2H_SWORD, quantity = 1, slots = 4)
            obj(Items.MITHRIL_AXE, quantity = 1, slots = 4)
            obj(Items.MITHRIL_BATTLEAXE, quantity = 1, slots = 4)
            obj(Items.RUNE_LONGSWORD, quantity = 1, slots = 2)
            obj(Items.ADAMANT_PLATEBODY, quantity = 1, slots = 2)
            obj(Items.RUNE_ARROW, quantityRange = 1..12, slots = 1)
            obj(Items.BLOOD_RUNE, quantityRange = 1..12, slots = 1)
            obj(Items.DEATH_RUNE, quantityRange = 1..19, slots = 1)
            obj(Items.COINS, quantityRange = 97..1287, slots = 2)
            obj(Items.ADAMANTITE_BAR, quantity = 1, slots = 2)
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
            hitpoints = 190
            attack = 200
            strength = 200
            defence = 200
            magic = 100
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
