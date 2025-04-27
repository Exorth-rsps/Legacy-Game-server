package org.alter.plugins.content.npcs.golems;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.RUNITE_GOLEM
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BIG_BONES, quantity = 1)
        }
        main {
            total(1024)
            obj(Items.MUDDY_KEY, quantity = 1, slots = 2)
            obj(Items.UNCUT_OPAL_NOTED, quantity = 1, slots = 8)
            obj(Items.UNCUT_JADE_NOTED, quantity = 1, slots = 8)
            obj(Items.UNCUT_RED_TOPAZ_NOTED, quantity = 1, slots = 8)
            obj(Items.UNCUT_SAPPHIRE_NOTED, quantity = 1, slots = 4)
            obj(Items.UNCUT_EMERALD_NOTED, quantity = 1, slots = 4)
            obj(Items.UNCUT_RUBY_NOTED, quantity = 1, slots = 4)
            obj(Items.UNCUT_DIAMOND_NOTED, quantity = 1, slots = 2)
            obj(Items.RUNE_FULL_HELM_T, quantity = 1, slots = 1)
            obj(Items.RUNE_KITESHIELD_T, quantity = 1, slots = 1)
            obj(Items.RUNE_PLATESKIRT_T, quantity = 1, slots = 1)
            obj(Items.RUNE_PLATELEGS_T, quantity = 1, slots = 1)
            obj(Items.RUNE_PLATEBODY_T, quantity = 1, slots = 1)
            obj(Items.RUNE_BOOTS, quantity = 1, slots = 1)
            obj(Items.RUNE_SCIMITAR, quantity = 1, slots = 1)
            obj(Items.RUNE_PICKAXE, quantity = 1, slots = 1)
            nothing(128)
        }
    }



table.register(droptable, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> The Runite Golem has been slain by $playerName!", ChatMessageType.CONSOLE)
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
            hitpoints = 270
            attack = 140
            strength = 150
            defence = 6165
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