package org.alter.plugins.content.npcs.human

import org.alter.api.cfg.Animation
import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs
import org.alter.api.cfg.Sound
import org.alter.game.model.entity.Player
import org.alter.plugins.content.drops.DropTableFactory

val ancientWizardMageIds = intArrayOf(
    Npcs.ANCIENT_WIZARD
)

private val ancientWizardTable = DropTableFactory
val ancientWizardDrops =
    ancientWizardTable.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        table("main") {
            total(683)
            obj(Items.GRIMY_TARROMIN, quantity = 1, 7)
            obj(Items.GRIMY_HARRALANDER, quantity = 1, 6)
            obj(Items.GRIMY_RANARR_WEED, quantity = 1, 4)
            obj(Items.GRIMY_IRIT_LEAF, quantity = 1, 3)
            obj(Items.GRIMY_AVANTOE, quantity = 1, 2)
            obj(Items.GRIMY_KWUARM, quantity = 1, 2)
            obj(Items.GRIMY_CADANTINE, quantity = 1, 2)
            obj(Items.GRIMY_LANTADYME, quantity = 1, 1)
            obj(Items.GRIMY_DWARF_WEED, quantity = 1, 1)
            obj(Items.POTATO_SEED_5318, quantityRange = 1..4, 15)
            obj(Items.FIRE_BATTLESTAFF, quantity = 1, 10)
            obj(Items.ONION_SEED_5319, quantityRange = 1..3, 8)
            obj(Items.CABBAGE_SEED_5324, quantityRange = 1..3, 4)
            obj(Items.TOMATO_SEED_5322, quantityRange = 1..2, 2)
            obj(Items.SWEETCORN_SEED_5320, quantityRange = 1..2, 1)
            obj(Items.STRAWBERRY_SEED_5323, quantity = 1, 1)
            obj(Items.WATERMELON_SEED_5321, quantity = 1, 1)
            obj(Items.SNAPE_GRASS_SEED, quantity = 1, 1)
            obj(Items.COINS, quantityRange = 50..249, 102)
            obj(Items.PRAYER_POTION4, quantity = 1, 20)
            obj(Items.PURE_ESSENCE, quantity = 25, 72)
            obj(Items.STAFF_OF_FIRE, quantity = 1, 10)
            obj(Items.RUNITE_CROSSBOW_U, quantity = 1, 10)
            obj(Items.LOOTING_BAG, quantity = 1, 171)
            obj(Items.AIR_RUNE, quantityRange = 5..24, 51)
            obj(Items.FIRE_RUNE, quantityRange = 5..24, 51)
            obj(Items.DEATH_RUNE, quantityRange = 5..24, 51)
            obj(Items.RUNITE_BOLTS, quantityRange = 1..5, 51)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, 13)
            obj(Items.GRIMY_MARRENTILL, quantity = 1, 10)
        }
    }

ancientWizardTable.register(ancientWizardDrops, *ancientWizardMageIds)

on_npc_pre_death(*ancientWizardMageIds) {
    npc.damageMap.getMostDamage() as? Player
}

on_npc_death(*ancientWizardMageIds) {
    ancientWizardTable.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}

ancientWizardMageIds.forEach { id ->
    set_combat_def(id) {
        configs {
            attackSpeed = 4
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 80
            attack = 50
            strength = 30
            defence = 20
            magic = 150
            ranged = 1
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
