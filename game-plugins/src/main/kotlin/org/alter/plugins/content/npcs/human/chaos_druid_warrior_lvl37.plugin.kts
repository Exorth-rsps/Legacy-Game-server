package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.SOLDIER_5421
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.COINS_995, quantityRange = 1..30, slots = 16)
            obj(Items.BLACK_DAGGER, quantity = 1, slots = 4)
            obj(Items.FIRE_RUNE, quantityRange = 1..9, slots = 4)
            obj(Items.AIR_RUNE, quantityRange = 1..14, slots = 4)
            obj(Items.EARTH_RUNE, quantityRange = 1..12, slots = 4)
            obj(Items.LIMPWURT_ROOT, quantityRange = 1..2, slots = 2)
            obj(Items.WHITE_BERRIES, quantityRange = 1..2, slots = 2)
            obj(Items.UNICORN_HORN_DUST, quantity = 1, slots = 2)
            obj(Items.SNAPE_GRASS, quantity = 1, slots = 2)
            obj(Items.VIAL_OF_WATER, quantity = 1, slots = 16)
            obj(Items.UNCUT_SAPPHIRE, quantity = 1, slots = 1)
            obj(Items.UNCUT_RUBY, quantity = 1, slots = 1)
            obj(Items.UNCUT_EMERALD, quantity = 1, slots = 1)
            obj(Items.UNCUT_DIAMOND, quantity = 1, slots = 1)
            nothing(16)
        }
        table("Herbs") {
            total(512)
            nothing(slots = 4)
            obj(Items.GRIMY_GUAM_LEAF, quantity = 1, slots = 4)
            obj(Items.GRIMY_MARRENTILL, quantity = 1, slots = 4)
            obj(Items.GRIMY_TARROMIN, quantity = 1, slots = 4)
            obj(Items.GRIMY_HARRALANDER, quantity = 1, slots = 2)
            obj(Items.GRIMY_RANARR_WEED, quantity = 1, slots = 2)
            obj(Items.GRIMY_IRIT_LEAF, quantity = 1, slots = 2)
            obj(Items.GRIMY_AVANTOE, quantity = 1, slots = 1)
            obj(Items.GRIMY_KWUARM, quantity = 1, slots = 1)
            obj(Items.GRIMY_CADANTINE, quantity = 1, slots = 1)
            obj(Items.GRIMY_LANTADYME, quantity = 1, slots = 1)
            obj(Items.GRIMY_DWARF_WEED, quantity = 1, slots = 1)
        }
    }


table.register(droptable, *ids)

on_npc_pre_death(*ids) {
    val p = npc.damageMap.getMostDamage()!! as Player
}

on_npc_death(*ids) {
    table.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}


ids.forEach {
    set_combat_def(it) {
        configs {
            attackSpeed = 5
            respawnDelay = 50
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 40
            attack = 32
            strength = 34
            defence = 25
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 13
            defenceSlash = 17
            defenceCrush = 14
            defenceMagic = -4
            defenceRanged = 14
        }


        anims {
            attack = Animation.HUMAN_SLASH_SWORD_ATTACK
            block = Animation.HUMAN_SLASH_SWORD_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_BLOCK_1
            deathSound = Sound.HUMAN_DEATH
        }
    }
}