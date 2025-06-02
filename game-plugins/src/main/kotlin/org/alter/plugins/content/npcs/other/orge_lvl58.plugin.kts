package org.alter.plugins.content.npcs.ogres;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.OGRE_1153
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
        }
        table("Herbs") {
            total(512)
            nothing(slots = 4)
        }
        table("Herbs_Noted") {
            total(512)
            nothing(slots = 4)
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
            attackSpeed = 6
            respawnDelay = 25
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 70
            attack = 46
            strength = 48
            defence = 43
            magic = 1
            ranged = 1
        }


        anims {
            attack = Animation.OGRE_ATTACK
            block = Animation.OGRE_DEFEND
            death = Animation.OGRE_DEATH
        }

        sound {
            attackSound = Sound.ZOGRE_ATTACK
            blockSound = Sound.ZOGRE_HIT
            deathSound = Sound.ZOGRE_DEATH
        }
    }
}