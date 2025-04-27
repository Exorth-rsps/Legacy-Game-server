package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.ICE_QUEEN
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(10240)
            obj(Items.ARROW_SHAFT, quantity = 1, slots = 20)
            obj(Items.FEATHER, quantity = 1, slots = 20)
            obj(Items.BRONZE_ARROWTIPS, quantity = 1, slots = 20)
            obj(Items.IRON_ARROWTIPS, quantity = 1, slots = 20)
            obj(Items.STEEL_ARROWTIPS, quantity = 1, slots = 20)
            obj(Items.CRYSTAL_BOW, quantity = 1, slots = 1)
            obj(Items.CRYSTAL_SHIELD, quantity = 1, slots = 1)
            obj(Items.CRYSTAL_BODY, quantity = 1, slots = 1)
            obj(Items.CRYSTAL_LEGS, quantity = 1, slots = 1)
            obj(Items.CRYSTAL_HALBERD, quantity = 1, slots = 1)
            obj(Items.CRYSTAL_HELM, quantity = 1, slots = 1)
            obj(Items.ICE_GLOVES, quantity = 1, slots = 2)
            nothing(1280)
        }
    }


table.register(droptable, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> Ice Queen has been slain by $playerName!", ChatMessageType.CONSOLE)
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
            hitpoints = 304
            attack = 185
            strength = 184
            defence = 185
            magic = 120
            ranged = 1
        }

        bonuses {
            defenceStab = 60
            defenceSlash = 80
            defenceCrush = 20
            defenceMagic = 20
            defenceRanged = 60
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