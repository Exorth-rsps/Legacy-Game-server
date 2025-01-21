package org.alter.plugins.content.npcs.trolls.dad;

import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.DAD_6391
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.JOGRE_BONES)
        }
        main {
            total(1024)
            obj(Items.COINS_995, quantity = 1, slots = 32)
            obj(Items.COINS_995, quantity = 1, slots = 8)
            obj(Items.RUNE_PLATELEGS, quantity = 1, slots = 4)
            obj(Items.RUNE_PLATESKIRT, quantity = 10, slots = 4)
            obj(Items.RUNE_PLATEBODY, quantity = 15, slots = 2)
            obj(Items.RUNE_CHAINBODY, quantity = 5, slots = 8)
            obj(Items.WHITE_BERRIES, quantity = 1, slots = 4)
            obj(Items.RUNE_BOOTS, quantity = 5, slots = 8)
            obj(Items.RUNE_SCIMITAR, quantity = 5, slots = 2)
            obj(Items.RUNE_2H_SWORD, quantity = 1, slots = 2)
            obj(Items.RUNE_BATTLEAXE, quantity = 1, slots = 2)
            obj(Items.RUNE_MACE, quantity = 1, slots = 8)
            obj(Items.RUNE_GLOVES, quantity = 1, slots = 8)
            obj(Items.RUNE_SWORD, quantity = 1, slots = 8)
            obj(Items.RUNE_LONGSWORD, quantity = 1, slots = 4)
            obj(Items.MAPLE_SHORTBOW, quantity = 1, slots = 8)
            obj(Items.STEEL_ARROW, quantity = 1, slots = 8)
            obj(Items.LOBSTER, quantity = 1, slots = 32)
            obj(Items.SWORDFISH, quantity = 1, slots = 32)
            obj(Items.TUNA, quantity = 1, slots = 32)
            obj(Items.IRON_ORE, quantity = 1, slots = 32)
            obj(Items.COPPER_ORE, quantity = 1, slots = 128)
            obj(Items.TIN_ORE, quantity = 1, slots = 128)
            obj(Items.RUNE_ESSENCE, quantity = 1, slots = 128)
            obj(Items.RUNE_ESSENCE_NOTED, quantity = 5, slots = 2)
            nothing(slots = 128)
        }

        table("Hebs") {
            total(1024)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, slots = 16)
            obj(Items.GRIMY_MARRENTILL, quantity = 1, slots = 16)
            obj(Items.GRIMY_TARROMIN, quantity = 1, slots = 4)
            obj(Items.GRIMY_HARRALANDER, quantity = 1, slots = 4)
            obj(Items.GRIMY_RANARR_WEED, quantity = 1, slots = 1)
            nothing(slots = 64)
        }
        table("Rares") {
            total(5000)
            obj(Items.KEY_1543, quantity = 1, slots = 1)
            obj(Items.GRANITE_SHIELD, quantity = 1, slots = 1)
            obj(Items.GRANITE_MAUL, quantity = 1, slots = 1)
            obj(Items.GRANITE_LONGSWORD, quantity = 1, slots = 1)
            obj(Items.GRANITE_HAMMER, quantity = 1, slots = 1)
            obj(Items.GRANITE_HELM, quantity = 1, slots = 1)
            obj(Items.GRANITE_BODY, quantity = 1, slots = 1)
            obj(Items.GRANITE_LEGS, quantity = 1, slots = 1)
            obj(Items.GRANITE_GLOVES, quantity = 1, slots = 1)
            obj(Items.GRANITE_BOOTS, quantity = 1, slots = 1)
            obj(Items.GRIMY_TARROMIN_NOTED, quantityRange = 5..10, slots = 2)
            obj(Items.LIMPWURT_ROOT_NOTED, quantityRange = 8..15, slots = 2)
            obj(Items.GRIMY_KWUARM_NOTED, quantityRange = 2..5, slots = 2)
            obj(Items.WHITE_BERRIES, quantityRange = 5..12, slots = 2)
            obj(Items.COAL_NOTED, quantityRange = 5..35, slots = 2)
            nothing(slots = 128)
        }
    }


table.register(droptable, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
    val playerName = p.username
    p.world.players.forEach {
        it.message("<col=8900331>[GLOBAL]</col> Dad has been slain by $playerName!", ChatMessageType.CONSOLE)
    }
}

on_npc_death(*ids) {
    table.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}


ids.forEach {
    set_combat_def(it) {
        configs {
            attackSpeed = 3
            respawnDelay = 6
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 120
            attack = 60
            strength = 120
            defence = 50
            magic = 0
            ranged = 0
        }

        bonuses {
            defenceStab = 25
            defenceSlash = 25
            defenceCrush = 40
            defenceMagic = 200
            defenceRanged = 200
        }

        anims {
            attack = Animation.TROLL_ATTACK
            block = Animation.TROLL_DEFEND
            death = Animation.DAD_DEATH
        }

        sound {
            attackSound = Sound.TROLL_ATTACK
            blockSound = Sound.TROLL_HIT
            deathSound = Sound.TROLL_CHAMPION_DEATH
        }
    }
}