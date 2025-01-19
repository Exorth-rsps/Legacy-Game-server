package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.WATCHMAN_5420
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(512)
            nothing(64)
            obj(Items.COINS_995, quantity = 1, slots = 16)
            obj(Items.BREAD, quantity = 1, slots = 16)
            obj(Items.MITHRIL_MED_HELM, quantity = 1, slots = 2)
            obj(Items.MITHRIL_CHAINBODY, quantity = 1, slots = 4)
            obj(Items.MITHRIL_SWORD, quantity = 1, slots = 4)
            obj(Items.MITHRIL_FULL_HELM, quantity = 1, slots = 1)
            obj(Items.MITHRIL_PLATELEGS, quantity = 1, slots = 1)
            obj(Items.MITHRIL_PLATESKIRT, quantity = 1, slots = 1)
            obj(Items.MITHRIL_PLATEBODY, quantity = 1, slots = 1)
            obj(Items.MITHRIL_2H_SWORD, quantity = 1, slots = 1)
            obj(Items.MITHRIL_KITESHIELD, quantity = 1, slots = 1)
            obj(Items.MITHRIL_SQ_SHIELD, quantity = 1, slots = 2)
            obj(Items.MITHRIL_SCIMITAR, quantity = 1, slots = 1)
            obj(Items.MITHRIL_BATTLEAXE, quantity = 1, slots = 1)
        }
        table("Herbs") {
            total(256)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, slots = 4)
            obj(Items.GRIMY_HARRALANDER, quantity = 1, slots = 2)
            obj(Items.GRIMY_TARROMIN, quantity = 1, slots = 1)
            nothing(slots = 64)
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
            respawnDelay = 50
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 22
            attack = 31
            strength = 31
            defence = 31
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 24
            defenceSlash = 14
            defenceCrush = 19
            defenceMagic = -4
            defenceRanged = 16
        }


        anims {
            attack = Animation.HUMAN_PUNCH
            block = Animation.HUMAN_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_BLOCK_1
            deathSound = Sound.HUMAN_DEATH
        }
    }
}