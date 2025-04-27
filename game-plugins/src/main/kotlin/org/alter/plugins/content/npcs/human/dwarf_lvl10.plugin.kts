package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.DWARF_290
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.COPPER_ORE, quantity = 1, slots = 4)
            obj(Items.TIN_ORE, quantity = 1, slots = 4)
            obj(Items.IRON_ORE, quantity = 1, slots = 2)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, slots = 1)
            obj(Items.STEEL_PICKAXE, quantity = 1, slots = 1)
            obj(Items.MUDDY_KEY, quantity = 1, slots = 1)
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
        configs {
            attackSpeed = 5
            respawnDelay = 25
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 16
            attack = 8
            strength = 8
            defence = 6
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 0
            defenceSlash = 0
            defenceCrush = 0
            defenceMagic = 5
            defenceRanged = 0
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