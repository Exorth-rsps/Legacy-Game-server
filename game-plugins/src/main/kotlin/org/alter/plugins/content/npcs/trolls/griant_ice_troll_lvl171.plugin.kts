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
        table("rare") {
            total(128)
            obj(Items.FROZEN_KEY, quantity = 1, 1)
            obj(Items.INFINITY_TOP, quantity = 1, 1)
            obj(Items.MASTER_WAND, quantity = 1, 1)
            obj(Items.INFINITY_HAT, quantity = 1, 1)
            obj(Items.INFINITY_BOOTS, quantity = 1, 1)
            obj(Items.INFINITY_GLOVES, quantity = 1, 1)
            obj(Items.INFINITY_BOTTOMS, quantity = 1, 1)
            obj(Items.BLURITE_SWORD, quantity = 1, 1)
            nothing(100)
        }
        table("second") {
            total(128)
            obj(Items.PURE_ESSENCE_NOTED, quantityRange = 1..20, 10)
            obj(Items.COAL_NOTED, quantityRange = 1..10, 10)
            obj(Items.UNCUT_DIAMOND, quantity = 1, 10)
            nothing(20)
        }
        table("gems") {
            total(128)
            obj(Items.UNCUT_RUBY, quantity = 1, 15)
            nothing(20)
        }
        table("secondary") {
            total(128)
            obj(Items.OAK_LOGS_NOTED, quantityRange = 1..5, 7)
            obj(Items.WILLOW_LOGS_NOTED, quantityRange = 1..5, 4)
            nothing(20)
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