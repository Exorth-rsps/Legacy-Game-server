package org.alter.plugins.content.npcs.dragons

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.GREEN_DRAGON,
)
val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.DRAGON_BONES, quantity = 1)
            obj(Items.GREEN_DRAGONHIDE, quantity = 1)
        }
        table("herbs-noted") {
            total(128)
            obj(Items.GRIMY_LANTADYME_NOTED, quantityRange = 3..8, 12)
            obj(Items.GRIMY_GUAM_LEAF_NOTED, quantityRange = 3..8, 20)
            obj(Items.GRIMY_MARRENTILL_NOTED, quantityRange = 3..8, 24)
            obj(Items.GRIMY_HARRALANDER_NOTED, quantityRange = 3..8, 22)
            nothing(50)
        }
        table("gems") {
            total(128)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, 90)
            obj(Items.UNCUT_EMERALD, quantity = 1, 80)
            nothing(90)
        }
        table("second") {
            total(128)
            obj(Items.LIMPWURT_ROOT_NOTED, quantityRange = 2..6, 30)
            obj(Items.RED_SPIDERS_EGGS_NOTED, quantityRange = 2..6, 28)
            nothing(70)
        }
        table("rare") {
            total(128)
            obj(Items.WHITE_BERRIES_NOTED, quantityRange = 2..6, 18)
            obj(Items.SNAPE_GRASS_NOTED, quantityRange = 2..6, 20)
            nothing(90)
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
            hitpoints = 75
            attack = 68
            strength = 68
            defence = 68
            magic = 68
            ranged = 1
        }

        bonuses {
            defenceStab = 10
            defenceSlash = 40
            defenceCrush = 40
            defenceMagic = 30
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
