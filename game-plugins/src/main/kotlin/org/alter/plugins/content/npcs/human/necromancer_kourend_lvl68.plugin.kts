package org.alter.plugins.content.npcs.human

import org.alter.api.cfg.Animation
import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs
import org.alter.api.cfg.Sound
import org.alter.game.model.entity.Player
import org.alter.plugins.content.drops.DropTableFactory

val kourendNecromancerIds = intArrayOf(
    Npcs.NECROMANCER_11088
)

private val kourendTable = DropTableFactory
val kourendDrops =
    kourendTable.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        table("main") {
            total(256)
            obj(Items.NATURE_RUNE, quantityRange = 10..15, 15)
            obj(Items.LAW_RUNE, quantityRange = 10..15, 15)
            obj(Items.DEATH_RUNE, quantityRange = 4..8, 10)
            obj(Items.BLOOD_RUNE, quantityRange = 4..8, 10)
            obj(Items.COINS, quantityRange = 10..50, 10)
            obj(Items.STAFF, quantity = 1, 10)
            obj(Items.AIR_TALISMAN, quantity = 1, 5)
            obj(Items.WATER_TALISMAN, quantity = 1, 5)
            obj(Items.EARTH_TALISMAN, quantity = 1, 5)
            obj(Items.FIRE_TALISMAN, quantity = 1, 5)
            obj(Items.AIR_RUNE, quantityRange = 20..30, 20)
            obj(Items.CLUE_SCROLL_MEDIUM, quantity = 1, 2)
            obj(Items.WATER_RUNE, quantityRange = 20..30, 20)
            obj(Items.EARTH_RUNE, quantityRange = 20..30, 20)
            obj(Items.FIRE_RUNE, quantityRange = 20..30, 20)
            obj(Items.MIND_RUNE, quantityRange = 20..30, 20)
            obj(Items.BODY_RUNE, quantityRange = 20..30, 20)
            obj(Items.COSMIC_RUNE, quantityRange = 10..15, 15)
            obj(Items.CHAOS_RUNE, quantityRange = 10..15, 15)
            nothing(14)
        }
    }

kourendTable.register(kourendDrops, *kourendNecromancerIds)

on_npc_pre_death(*kourendNecromancerIds) {
    npc.damageMap.getMostDamage() as? Player
}

on_npc_death(*kourendNecromancerIds) {
    kourendTable.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}

kourendNecromancerIds.forEach { id ->
    set_combat_def(id) {
        configs {
            attackSpeed = 4
            respawnDelay = 30
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 80
            attack = 56
            strength = 56
            defence = 50
            magic = 60
            ranged = 1
        }
        bonuses {
            attackMagic = 10
            defenceStab = 8
            defenceSlash = 8
            defenceCrush = 8
            defenceMagic = 4
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
