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
            obj(Items.DRAGON_BONES, quantity = 1)
            obj(Items.COINS, quantity = 4650)
        }
        table("rare") {
            total(128)
            obj(Items.DINHS_BULWARK, quantity = 1, 2)
            obj(Items.UNCUT_ONYX, quantity = 1, 4)
            nothing(100)
        }
        table("herbs-noted") {
            total(128)
            obj(Items.GRIMY_TORSTOL, quantity = 11, 4)
            obj(Items.GRIMY_TOADFLAX, quantity = 16, 4)
            obj(Items.CRUSHED_NEST, quantity = 9, 7)
            nothing(20)
        }
        table("gems") {
            total(128)
            obj(Items.UNCUT_DIAMOND, quantity = 6, 3)
            nothing(20)
        }
        table("coins") {
            total(128)
            obj(Items.COINS, quantity = 5530, 8)
            obj(Items.COINS, quantity = 6255, 8)
            obj(Items.COINS, quantity = 9850, 8)
            nothing(60)
        }
        table("herb-secondaries") {
            total(128)
            obj(Items.JANGERBERRIES, quantity = 9, 7)
            nothing(20)
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
            respawnDelay = 300
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
