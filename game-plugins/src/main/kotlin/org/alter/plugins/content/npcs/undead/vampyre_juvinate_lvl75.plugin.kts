package org.alter.plugins.content.npcs.undead

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.VAMPYRE_JUVINATE_4442
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.COINS, quantityRange = 500..2000)
        }
        table("main") {
            total(128)
            obj(Items.MITHRIL_SCIMITAR, quantity = 1, 20)
            obj(Items.MITHRIL_LONGSWORD, quantity = 1, 18)
            obj(Items.MITHRIL_2H_SWORD, quantity = 1, 17)
            obj(Items.MITHRIL_WARHAMMER, quantity = 1, 16)
            obj(Items.MITHRIL_BATTLEAXE, quantity = 1, 15)
            obj(Items.ADAMANT_MACE, quantity = 1, 13)
            obj(Items.ADAMANT_SCIMITAR, quantity = 1, 12)
            obj(Items.STAFF_OF_AIR, quantity = 1, 3)
            obj(Items.STAFF_OF_EARTH, quantity = 1, 5)
            nothing(20)
        }
        table("second") {
            total(128)
            obj(Items.MITHRIL_PLATELEGS, quantity = 1, 21)
            obj(Items.ADAMANT_MED_HELM, quantity = 1, 22)
            obj(Items.STEEL_PLATELEGS, quantity = 1, 23)
            obj(Items.STEEL_PLATEBODY, quantity = 1, 24)
            obj(Items.STEEL_PLATESKIRT, quantity = 1, 25)
            obj(Items.MITHRIL_KITESHIELD, quantity = 1, 26)
            nothing(20)
        }
        table("herbs-noted") {
            total(128)
            obj(Items.WILLOW_LOGS, quantity = 5, 20)
            obj(Items.YEW_LOGS, quantity = 5, 12)
            nothing(20)
        }
        table("rare") {
            total(128)
            obj(Items.NATURE_RUNE, quantity = 10, 12)
            obj(Items.AMULET_OF_MAGIC, quantity = 1, 1)
            nothing(20)
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
            respawnDelay = 40
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 55
            attack = 50
            strength = 48
            defence = 45
            magic = 1
            ranged = 1
        }

        anims {
            attack = Animation.VAMPIRE_ATTACK
            block = Animation.HUMAN_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.VAMPIRE_ATTACK
            blockSound = Sound.VAMPIRE_HIT
            deathSound = Sound.VAMPIRE_DEATH
        }
    }
}

