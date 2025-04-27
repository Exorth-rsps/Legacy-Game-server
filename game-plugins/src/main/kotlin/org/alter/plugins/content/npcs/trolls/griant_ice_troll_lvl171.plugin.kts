package org.alter.plugins.content.npcs.trolls;

import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.GIANT_ICE_TROLL
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
        }
        main {
            total(4096)
            obj(Items.RAW_SHRIMPS_NOTED, quantityRange = 1..50, slots = 32)
            obj(Items.RAW_TROUT_NOTED, quantityRange = 1..50, slots = 32)
            obj(Items.RAW_TUNA_NOTED, quantityRange = 1..25, slots = 16)
            obj(Items.RAW_LOBSTER_NOTED, quantityRange = 1..10, slots = 16)
            obj(Items.RAW_SWORDFISH_NOTED, quantityRange = 1..5, slots = 8)
            obj(Items.FROZEN_KEY, quantity = 1, slots = 1)
            nothing(512)
        }
        }




table.register(droptable, *ids)
on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> The Giant Ice Troll has been slain by $playerName!", ChatMessageType.CONSOLE)
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
            hitpoints = 280
            attack = 180
            strength = 190
            defence = 180
            magic = 75
            ranged = 75
        }

        bonuses {
            defenceStab = 30
            defenceSlash = 60
            defenceCrush = 30
            defenceMagic = 75
            defenceRanged = 75
        }

        anims {
            attack = Animation.TROLL_ATTACK
            block = Animation.TROLL_DEFEND
            death = Animation.TROLL_DEATH
        }

        sound {
            attackSound = Sound.TROLL_ATTACK
            blockSound = Sound.TROLL_HIT
            deathSound = Sound.TROLL_DEATH
        }
    }
}