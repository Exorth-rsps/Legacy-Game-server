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
        main {
            total(512)
            obj(Items.COINS_995, quantity = 1, slots = 16)
            obj(Items.EYE_OF_NEWT, quantity = 1, slots = 1)
            obj(Items.WHITE_BERRIES, quantity = 1, slots = 1)
            nothing(64)
        }
        table("Hebs") {
            total(512)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, slots = 16)
            obj(Items.GRIMY_MARRENTILL, quantity = 1, slots = 16)
            obj(Items.GRIMY_TARROMIN, quantity = 1, slots = 4)
            obj(Items.GRIMY_HARRALANDER, quantity = 1, slots = 4)
            obj(Items.GRIMY_RANARR_WEED, quantity = 1, slots = 1)
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
            attackSpeed = 6
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 60
            attack = 43
            strength = 43
            defence = 43
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