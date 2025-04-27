package org.alter.plugins.content.npcs.other;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.ICELORD_854,
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.RUNE_MED_HELM, quantity = 1, slots = 1)
            obj(Items.RUNE_SQ_SHIELD, quantity = 1, slots = 1)
            obj(Items.RUNE_SWORD, quantity = 1, slots = 1)
            obj(Items.CHAOS_RUNE, quantityRange = 1..10, slots = 2)
            obj(Items.DEATH_RUNE, quantityRange = 1..5, slots = 1)
            obj(Items.WINE_OF_ZAMORAK, quantity = 1, slots = 2)
            obj(Items.LIMPWURT_ROOT, quantity = 1, slots = 2)
            obj(Items.EYE_OF_NEWT, quantity = 1, slots = 4)
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
            respawnDelay = 100
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 60
            attack = 40
            strength = 40
            defence = 40
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
            attack = Animation.ICE_LORD_ATTACK2
            block = Animation.ICE_LORD_HIT
            death = Animation.ICE_LORD_DEATH
        }

        sound {
            attackSound = Sound.GIANT_ATTACK
            blockSound = Sound.GIANT_HIT
            deathSound = Sound.GIANT_DEATH
        }
    }
}