package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.ELDER_CHAOS_DRUID
)

val table = DropTableFactory
val droptable =
    table.build {
        table("rare") {
            total(128)
            obj(Items.ZAMORAK_MONK_BOTTOM, quantity = 1, 1)
            obj(Items.ZAMORAK_MONK_TOP, quantity = 1, 1)
            obj(Items.ELDER_CHAOS_TOP, quantity = 1, 1)
            obj(Items.ELDER_CHAOS_ROBE, quantity = 1, 1)
            obj(Items.ELDER_CHAOS_HOOD, quantity = 1, 1)
            obj(Items.ANCIENT_STAFF, quantity = 1, 3)
            obj(Items.ZURIELS_STAFF, quantity = 1, 1)
            nothing(119)
        }
        table("main") {
            total(128)
            obj(Items.AIR_RUNE, quantityRange = 10..25, 10)
            obj(Items.MIND_RUNE, quantityRange = 10..25, 10)
            obj(Items.EARTH_RUNE, quantityRange = 10..25, 10)
            obj(Items.WATER_RUNE, quantityRange = 10..20, 10)
            obj(Items.FIRE_RUNE, quantityRange = 10..20, 10)
            obj(Items.BODY_RUNE, quantityRange = 10..20, 10)
            obj(Items.NATURE_RUNE, quantityRange = 10..15, 10)
            obj(Items.BLOOD_RUNE, quantityRange = 10..15, 10)
            obj(Items.DEATH_RUNE, quantityRange = 10..15, 10)
            obj(Items.CHAOS_RUNE, quantityRange = 10..15, 10)
            obj(Items.LAW_RUNE, quantityRange = 10..15, 10)
            nothing(30)
        }
    }


table.register(droptable, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> The Elder Chaos Druid has been slain by $playerName!", ChatMessageType.CONSOLE)
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
            poisonChance = 10.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 350
            attack = 87
            strength = 98
            defence = 65
            magic = 110
            ranged = 1
        }


        anims {
            attack = Animation.HUMAN_PUNCH
            block = Animation.HUMAN_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_HIT5
            deathSound = Sound.HUMAN_DEATH
        }
    }
}