package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.HIGHWAYMAN
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(256)
            nothing(slots = 32)
            obj(Items.COINS_995, quantity = 1, slots = 8)
            obj(Items.BLACK_CAPE, quantity = 1, slots = 1)
            obj(Items.IRON_ARROW, quantity = 1, slots = 1)
            obj(Items.BRONZE_ARROW, quantity = 1, slots = 2)
        }
        table("Mask") {
            total(10240)
            nothing(slots = 1280)
            obj(Items.HIGHWAYMAN_MASK, quantity = 1, slots = 1)

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
            hitpoints = 13
            attack = 2
            strength = 2
            defence = 2
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 0
            defenceSlash = 3
            defenceCrush = 2
            defenceMagic = 0
            defenceRanged = 2
        }


        anims {
            attack = Animation.HUMAN_SLASH_SWORD_ATTACK
            block = Animation.HUMAN_SLASH_SWORD_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_BLOCK_1
            deathSound = Sound.HUMAN_DEATH
        }
    }
}