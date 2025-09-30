package org.alter.plugins.content.npcs.human

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.WIZARD_3257
)

val table = DropTableFactory
val droptable =
    table.build {
        table("main") {
            total(128)
            obj(Items.AIR_TALISMAN, quantity = 1, 20)
            obj(Items.EARTH_TALISMAN, quantity = 1, 20)
            obj(Items.FIRE_TALISMAN, quantity = 1, 20)
            obj(Items.WATER_TALISMAN, quantity = 1, 20)
            obj(Items.BODY_TALISMAN, quantity = 1, 15)
            obj(Items.MIND_TALISMAN, quantity = 1, 15)
            obj(Items.BLOOD_TALISMAN, quantity = 1, 1)
            nothing(30)
        }
        table("second") {
            total(128)
            obj(Items.CHAOS_TALISMAN, quantity = 1, 10)
            obj(Items.COSMIC_TALISMAN, quantity = 1, 10)
            obj(Items.DEATH_TALISMAN, quantity = 1, 1)
            nothing(80)
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
            respawnDelay = 25
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 14
            attack = 8
            strength = 8
            defence = 5
            magic = 10
            ranged = 1
        }
        bonuses {
            defenceStab = 0
            defenceSlash = 0
            defenceCrush = 0
            defenceMagic = 3
            defenceRanged = 0
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
