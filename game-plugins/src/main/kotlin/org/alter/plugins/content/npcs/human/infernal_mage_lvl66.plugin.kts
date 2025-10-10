package org.alter.plugins.content.npcs.human

import org.alter.api.cfg.Animation
import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs
import org.alter.api.cfg.Sound
import org.alter.game.model.entity.Player
import org.alter.plugins.content.drops.DropTableFactory

val infernalMageIds = intArrayOf(
    Npcs.INFERNAL_MAGE_443,
    Npcs.INFERNAL_MAGE_444,
    Npcs.INFERNAL_MAGE_445,
    Npcs.INFERNAL_MAGE_446,
    Npcs.INFERNAL_MAGE_447
)

private val infernalMageTable = DropTableFactory
val infernalMageDrops =
    infernalMageTable.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        table("main") {
            total(512)
            obj(Items.AIR_RUNE, quantity = 10, 12)
            obj(Items.WATER_RUNE, quantity = 10, 12)
            obj(Items.AIR_RUNE, quantity = 18, 8)
            obj(Items.WATER_RUNE, quantity = 18, 8)
            obj(Items.EARTH_RUNE, quantity = 18, 8)
            obj(Items.FIRE_RUNE, quantity = 18, 8)
            obj(Items.MIND_RUNE, quantity = 18, 8)
            obj(Items.BODY_RUNE, quantity = 18, 8)
            obj(Items.BLOOD_RUNE, quantity = 4, 8)
            obj(Items.DEATH_RUNE, quantity = 7, 72)
            obj(Items.MYSTIC_BOOTS_DARK, quantity = 1, 1)
            obj(Items.COINS, quantity = 1, 76)
            obj(Items.COINS, quantity = 2, 56)
            obj(Items.COINS, quantity = 4, 32)
            obj(Items.COINS, quantity = 29, 12)
            obj(Items.MYSTIC_HAT_DARK, quantity = 1, 1)
            obj(Items.STAFF, quantity = 1, 32)
            obj(Items.STAFF_OF_FIRE, quantity = 1, 4)
            obj(Items.LAVA_BATTLESTAFF, quantity = 1, 1)
            obj(Items.EARTH_RUNE, quantity = 10, 24)
            obj(Items.FIRE_RUNE, quantity = 10, 24)
            obj(Items.EARTH_RUNE, quantity = 36, 16)
            nothing(81)
        }
    }

infernalMageTable.register(infernalMageDrops, *infernalMageIds)

on_npc_pre_death(*infernalMageIds) {
    npc.damageMap.getMostDamage() as? Player
}

on_npc_death(*infernalMageIds) {
    infernalMageTable.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}

infernalMageIds.forEach { id ->
    set_combat_def(id) {
        configs {
            attackSpeed = 4
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 60
            attack = 1
            strength = 1
            defence = 60
            magic = 75
            ranged = 1
        }
        bonuses {
            defenceMagic = 40
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
