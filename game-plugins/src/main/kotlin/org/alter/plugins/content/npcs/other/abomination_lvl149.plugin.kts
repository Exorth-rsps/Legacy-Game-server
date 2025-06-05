package org.alter.plugins.content.npcs.other

import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.drops.DropTableFactory
import org.alter.game.model.Tile

set_multi_combat_region(region = 13472)

spawn_npc(Npcs.ABOMINATION_8262, x = 3361, z = 10274, walkRadius = 5)


val ids = intArrayOf(
    Npcs.ABOMINATION_8262
)
val table = DropTableFactory
val npc =
    table.build {
        guaranteed {
        }
        main {
            total(10240)
            nothing(12)

        }

    }

table.register(npc, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> The Abomination has been slain by $playerName!", ChatMessageType.CONSOLE)
    }
}
on_npc_death(*ids) {
    table.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}
ids.forEach {
    set_combat_def(it) {

        configs {
            attackSpeed = 5
            respawnDelay = 3
            poisonChance = 0.0
            venomChance = 0.0
            followRange = 100
        }
        stats {
            hitpoints = 200
            attack = 110
            strength = 110
            defence = 110
            magic = 110
            ranged = 110
        }

        bonuses {
            defenceStab = 80
            defenceSlash = 80
            defenceCrush = 80
            defenceMagic = 20
            defenceRanged = 180
        }

        anims {
            attack = Animation.ABOMINATION_ATTACK
            block = Animation.ABOMINATION_HIT
            death = Animation.ABOMINATION_DEATH
        }

        sound {
            attackSound = Sound.SHADE_ATTACK
            blockSound = Sound.SHADE_HIT
            deathSound = Sound.SHADE_DEATH
        }
        aggro {
            radius = 10
        }

    }
}
