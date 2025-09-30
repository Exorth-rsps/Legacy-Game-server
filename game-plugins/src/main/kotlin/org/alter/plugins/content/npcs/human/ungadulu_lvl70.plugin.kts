package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.UNGADULU
)

val table = DropTableFactory
val droptable =
    table.build {
        table.build {
            guaranteed {
                obj(Items.BONES, quantity = 1)
            }
            table("rare") {
                total(128)
                obj(Items.BERSERKER_HELM, quantity = 1, 1)
                obj(Items.ARCHER_HELM, quantity = 1, 1)
                obj(Items.MIME_MASK, quantity = 1, 15)
                nothing(100)
            }
            table("second") {
                total(128)
                obj(Items.MIME_BOOTS, quantity = 1, 15)
                obj(Items.MIME_GLOVES, quantity = 1, 15)
                obj(Items.MIME_LEGS, quantity = 1, 15)
                obj(Items.MIME_TOP, quantity = 1, 15)
                nothing(100)
            }
            table("main") {
                total(128)
                obj(Items.GUAM_POTION_UNF_NOTED, quantityRange = 1..5, 5)
                obj(Items.MARRENTILL_POTION_UNF_NOTED, quantityRange = 1..5, 5)
                obj(Items.TARROMIN_POTION_UNF_NOTED, quantityRange = 1..5, 5)
                obj(Items.RANARR_POTION_UNF_NOTED, quantityRange = 1..4, 5)
                obj(Items.IRIT_POTION_UNF_NOTED, quantityRange = 1..4, 5)
                obj(Items.AVANTOE_POTION_UNF_NOTED, quantityRange = 1..4, 5)
                obj(Items.CADANTINE_POTION_UNF_NOTED, quantityRange = 1..4, 5)
                obj(Items.RUNE_SCIMITAR, quantity = 1, 11)
                nothing(20)
            }
        }
    }


table.register(droptable, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> Ungadulu has been slain by $playerName!", ChatMessageType.CONSOLE)
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
            hitpoints = 230
            attack = 57
            strength = 61
            defence = 65
            magic = 65
            ranged = 0
        }
        bonuses {
            defenceStab = 0
            defenceSlash = 0
            defenceCrush = 0
            defenceMagic = 55
            defenceRanged = 0
        }


        anims {
            attack = Animation.HUMAN_STAFF_BASH
            block = Animation.HUMAN_STAFF_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_HIT4
            deathSound = Sound.HUMAN_DEATH
        }
    }
}