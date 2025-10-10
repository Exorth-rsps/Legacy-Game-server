package org.alter.plugins.content.npcs.other

import org.alter.api.cfg.Animation
import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs
import org.alter.api.cfg.Sound
import org.alter.game.model.entity.Player
import org.alter.plugins.content.drops.DropTableFactory

val scarredLesserDemonIds = intArrayOf(
    Npcs.SCARRED_LESSER_DEMON,
    Npcs.SCARRED_LESSER_DEMON_12362,
    Npcs.SCARRED_LESSER_DEMON_12364,
    Npcs.SCARRED_LESSER_DEMON_12378,
    Npcs.SCARRED_LESSER_DEMON_12390
)

private val scarredDemonTable = DropTableFactory
val scarredDemonDrops =
    scarredDemonTable.build {
        guaranteed {
            obj(Items.VILE_ASHES, quantity = 1)
        }
        table("main") {
            total(697)
            obj(Items.LOOTING_BAG, quantity = 1, 171)
            obj(Items.COINS, quantity = 120, 160)
            obj(Items.COINS, quantity = 40, 116)
            obj(Items.COINS, quantity = 200, 40)
            obj(Items.FIRE_RUNE, quantity = 60, 32)
            obj(Items.COINS, quantity = 10, 28)
            obj(Items.CHAOS_RUNE, quantity = 12, 20)
            obj(Items.STEEL_FULL_HELM, quantity = 1, 16)
            obj(Items.STEEL_AXE, quantity = 1, 16)
            obj(Items.DEATH_RUNE, quantity = 3, 12)
            obj(Items.JUG_OF_WINE, quantity = 1, 12)
            obj(Items.STEEL_SCIMITAR, quantity = 1, 12)
            obj(Items.ENSOULED_DEMON_HEAD, quantity = 1, 10)
            obj(Items.GOLD_ORE, quantity = 1, 8)
            obj(Items.LARRANS_KEY_23490, quantity = 1, 5)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, 4)
            obj(Items.FIRE_RUNE, quantity = 30, 4)
            obj(Items.COINS, quantity = 450, 4)
            obj(Items.COINS, quantity = 10, 4)
            obj(Items.MITHRIL_SQ_SHIELD, quantity = 1, 4)
            obj(Items.MITHRIL_CHAINBODY, quantity = 1, 4)
            obj(Items.RUNE_MED_HELM, quantity = 1, 4)
            obj(Items.UNCUT_EMERALD, quantity = 1, 2)
            obj(Items.SLAYERS_ENCHANTMENT, quantity = 1, 2)
            obj(Items.ANCIENT_SHARD, quantity = 1, 2)
            obj(Items.DARK_TOTEM_BASE, quantity = 1, 1)
            obj(Items.DARK_TOTEM_MIDDLE, quantity = 1, 1)
            obj(Items.DARK_TOTEM_TOP, quantity = 1, 1)
            obj(Items.UNCUT_RUBY, quantity = 1, 1)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, 1)
        }
    }

scarredDemonTable.register(scarredDemonDrops, *scarredLesserDemonIds)

on_npc_pre_death(*scarredLesserDemonIds) {
    npc.damageMap.getMostDamage() as? Player
}

on_npc_death(*scarredLesserDemonIds) {
    scarredDemonTable.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}

scarredLesserDemonIds.forEach { id ->
    set_combat_def(id) {
        configs {
            attackSpeed = 4
            respawnDelay = 40
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 85
            attack = 68
            strength = 70
            defence = 71
            magic = 85
            ranged = 1
        }
        bonuses {
            defenceMagic = -10
        }
        anims {
            attack = Animation.DEMON_ATTACK
            block = Animation.DEMON_HIT
            death = Animation.DEMON_DEATH
        }
        sound {
            attackSound = Sound.DEMON_ATTACK
            blockSound = Sound.DEMON_HIT
            deathSound = Sound.DEMON_DEATH
        }
    }
}
