package org.alter.plugins.content.npcs.golems;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.ROCK_GOLEM_6728
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.MUDDY_KEY, quantity = 1, slots = 1)
            obj(Items.UNCUT_OPAL_NOTED, quantity = 1, slots = 4)
            obj(Items.UNCUT_JADE_NOTED, quantity = 1, slots = 4)
            obj(Items.UNCUT_RED_TOPAZ_NOTED, quantity = 1, slots = 4)
            obj(Items.UNCUT_SAPPHIRE_NOTED, quantity = 1, slots = 2)
            obj(Items.UNCUT_EMERALD_NOTED, quantity = 1, slots = 2)
            obj(Items.UNCUT_RUBY_NOTED, quantity = 1, slots = 2)
            obj(Items.UNCUT_DIAMOND_NOTED, quantity = 1, slots = 1)
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
            respawnDelay = 60
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 85
            attack = 65
            strength = 65
            defence = 65
            magic = 1
            ranged = 0
        }

        bonuses {
            defenceStab = 0
            defenceSlash = 0
            defenceCrush = 0
            defenceMagic = 0
            defenceRanged = 0
        }


        anims {
            attack = Animation.ROCK_GOLEM_ATTACK
            block = Animation.ROCK_GOLEM_HIT
            death = Animation.ROCK_GOLEM_DEATH
        }

        sound {
            attackSound = Sound.GIANT_ATTACK
            blockSound = Sound.GIANT_HIT
            deathSound = Sound.GIANT_DEATH
        }
    }
}