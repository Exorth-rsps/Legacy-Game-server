package org.alter.plugins.content.npcs.other;

import org.alter.plugins.content.drops.DropTableFactory

set_multi_combat_region(region = 11925)
set_multi_combat_region(region = 12181)
val ids = intArrayOf(
    Npcs.ICELORD_855,
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
        }
        main {
            total(1024)
            obj(Items.RUNE_MED_HELM, quantity = 1, slots = 2)
            obj(Items.RUNE_SQ_SHIELD, quantity = 1, slots = 2)
            obj(Items.RUNE_SWORD, quantity = 1, slots = 2)
            obj(Items.CHAOS_RUNE, quantityRange = 1..10, slots = 2)
            obj(Items.DEATH_RUNE, quantityRange = 1..5, slots = 1)
            obj(Items.WINE_OF_ZAMORAK, quantity = 1, slots = 2)
            obj(Items.LIMPWURT_ROOT, quantity = 1, slots = 2)
            obj(Items.EYE_OF_NEWT, quantity = 1, slots = 4)
            obj(Items.RUNE_PLATELEGS, quantity = 1, slots = 2)
            obj(Items.RUNE_PLATESKIRT, quantity = 1, slots = 2)
            obj(Items.RUNE_PLATEBODY, quantity = 1, slots = 2)
            obj(Items.RUNE_FULL_HELM, quantity = 1, slots = 2)
            obj(Items.RUNE_KITESHIELD, quantity = 1, slots = 2)
            obj(Items.RUNE_BATTLEAXE, quantity = 1, slots = 2)
            obj(Items.RUNE_SCIMITAR, quantity = 1, slots = 2)
            obj(Items.RUNE_2H_SWORD, quantity = 1, slots = 2)
            obj(Items.RUNE_WARHAMMER, quantity = 1, slots = 2)
            obj(Items.ICY_KEY, quantity = 1, slots = 1)
        }
    }



table.register(droptable, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> The Icelord Champion has been slain by $playerName!", ChatMessageType.CONSOLE)
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
            hitpoints = 220
            attack = 80
            strength = 80
            defence = 60
            magic = 10
            ranged = 10
        }

        bonuses {
            defenceStab = 5
            defenceSlash = 35
            defenceCrush = 5
            defenceMagic = -10
            defenceRanged = 200
        }


        anims {
            attack = Animation.ICE_LORD_ATTACK
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