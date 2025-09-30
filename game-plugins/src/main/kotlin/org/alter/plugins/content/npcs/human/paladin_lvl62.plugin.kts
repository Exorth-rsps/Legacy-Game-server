package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.PALADIN_3293
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        table("main") {
            total(128)
            obj(Items.COINS, quantityRange = 2..850, 22)
            obj(Items.UNCUT_DIAMOND, quantity = 1, 1)
            obj(Items.UNCUT_EMERALD, quantity = 1, 7)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, 11)
            obj(Items.UNCUT_RUBY, quantity = 1, 4)
            obj(Items.GRIMY_GUAM_LEAF_NOTED, quantityRange = 2..8, 9)
            obj(Items.GRIMY_IRIT_LEAF_NOTED, quantityRange = 2..8, 5)
            obj(Items.GRIMY_RANARR_WEED_NOTED, quantityRange = 1..7, 5)
            obj(Items.GRIMY_TARROMIN_NOTED, quantityRange = 1..6, 5)
            obj(Items.GRIMY_MARRENTILL_NOTED, quantityRange = 1..6, 5)
            obj(Items.IRON_BAR_NOTED, quantity = 1, 5)
            obj(Items.STEEL_BAR_NOTED, quantity = 1, 5)
            obj(Items.MITHRIL_BAR_NOTED, quantity = 1, 3)
            obj(Items.DRAGON_BATTLEAXE, quantity = 1, 1)
            nothing(60)
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
            respawnDelay = 20
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 57
            attack = 54
            strength = 54
            defence = 54
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 87
            defenceSlash = 84
            defenceCrush = 76
            defenceMagic = -10
            defenceRanged = 79
        }


        anims {
            attack = Animation.HUMAN_SLASH_SWORD_ATTACK
            block = Animation.HUMAN_SHIELD_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_BLOCK_1
            deathSound = Sound.HUMAN_DEATH
        }
    }
}