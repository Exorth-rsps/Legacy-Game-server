package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.DWARF_GANG_MEMBER_1356
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        table("main") {
            total(128)
            obj(Items.COAL, quantity = 1, 50)
            obj(Items.IRON_ORE, quantity = 1, 55)
            obj(Items.GOLD_BAR, quantity = 1, 20)
            obj(Items.ADAMANT_PICKAXE, quantity = 1, 17)
            obj(Items.RUNE_PICKAXE, quantity = 1, 14)
            nothing(30)
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
            hitpoints = 25
            attack = 30
            strength = 60
            defence = 57
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 7
            defenceSlash = 7
            defenceCrush = 0
            defenceMagic =0
            defenceRanged = 7
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