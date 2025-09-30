package org.alter.plugins.content.npcs.dragons

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.BLACK_DRAGON,
)
val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.DRAGON_BONES, quantity = 1)
            obj(Items.BLACK_DRAGONHIDE, quantity = 1)
        }
        table("main") {
            total(128)
            obj(Items.MITHRIL_BATTLEAXE, quantity = 1, 35)
            obj(Items.ADAMANT_BATTLEAXE, quantity = 1, 30)
            obj(Items.RUNE_BATTLEAXE, quantity = 1, 28)
            nothing(35)
        }
        table("rare") {
            total(128)
            obj(Items.DRACONIC_VISAGE, quantity = 1, 1)
            nothing(127)
        }
        table("herbs-noted") {
            total(128)
            obj(Items.GRIMY_LANTADYME_NOTED, quantityRange = 3..10, 12)
            obj(Items.GRIMY_RANARR_WEED_NOTED, quantityRange = 3..10, 10)
            obj(Items.GRIMY_GUAM_LEAF_NOTED, quantityRange = 3..10, 20)
            obj(Items.GRIMY_MARRENTILL_NOTED, quantityRange = 3..10, 24)
            obj(Items.GRIMY_HARRALANDER_NOTED, quantityRange = 3..10, 22)
            obj(Items.GRIMY_DWARF_WEED_NOTED, quantityRange = 3..10, 10)
            obj(Items.GRIMY_TORSTOL_NOTED, quantityRange = 3..10, 9)
            nothing(21)
        }
        table("gems") {
            total(128)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, 90)
            obj(Items.UNCUT_EMERALD, quantity = 1, 80)
            obj(Items.UNCUT_DIAMOND, quantity = 1, 70)
            obj(Items.UNCUT_RUBY, quantity = 1, 75)
            nothing(75)
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

    }
}
