package org.alter.plugins.content.npcs.other;

import org.alter.plugins.content.drops.DropTableFactory

set_multi_combat_region(region = 11929)
val ids = intArrayOf(
    Npcs.SLAGILITH,
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
            obj(Items.ADAMANTITE_ORE, quantity = 1)
            obj(Items.UNCUT_RUBY, quantity = 1)
        }
        main {
            total(1024)
            obj(Items.ROCKSHELL_HELM, quantity = 1, slots = 1)
            obj(Items.ROCKSHELL_PLATE, quantity = 1, slots = 1)
            obj(Items.ROCKSHELL_LEGS, quantity = 1, slots = 1)
            obj(Items.ROCKSHELL_BOOTS, quantity = 1, slots = 1)
            obj(Items.ROCKSHELL_GLOVES, quantity = 1, slots = 1)
            obj(Items.MUDDY_KEY, quantity = 1, slots = 2)
            nothing(128)
        }
    }



table.register(droptable, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> Slagilith has been slain by $playerName!", ChatMessageType.CONSOLE)
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
            hitpoints = 160
            attack = 60
            strength = 120
            defence = 75
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 50
            defenceSlash = 50
            defenceCrush = 5
            defenceMagic = 5
            defenceRanged = 50
        }


        anims {
            attack = Animation.SLAGILITH_ATTACK
            block = Animation.SLAGILITH_HIT
            death = Animation.SLAGILITH_DEATH
        }

        sound {
            attackSound = Sound.GIANT_ATTACK
            blockSound = Sound.GIANT_HIT
            deathSound = Sound.GIANT_DEATH
        }
    }
}