package org.alter.plugins.content.npcs.critters;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.CAVE_CRAWLER_406
)

val table = DropTableFactory
val droptable =
    table.build {
        main {
            total(128)
            nothing(16)
        }
        table("Herbs") {
            total(512)
            nothing(slots = 4)
        }
        table("Secondaries") {
            total(256)
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
            attackSpeed = 4
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 22
            attack = 18
            strength = 18
            defence = 18
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 10
            defenceSlash = 10
            defenceCrush = 5
            defenceMagic = 5
            defenceRanged = 10
        }

        anims {
            attack = Animation.CAVE_CRAWLER_ATTACK
            block = Animation.CAVE_CRAWLER_HIT
            death = Animation.CAVE_CRAWLER_DEATH
        }

        sound {
            attackSound = Sound.CAVE_CRAWLER_ATTACK
            blockSound = Sound.CAVE_CRAWLER_HIT
            deathSound = Sound.CAVE_CRAWLER_DEATH
        }
    }
}