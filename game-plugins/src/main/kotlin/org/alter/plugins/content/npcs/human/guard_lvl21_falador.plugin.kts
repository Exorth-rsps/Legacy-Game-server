package org.alter.plugins.content.npcs.human;

import org.alter.plugins.content.drops.DropTableFactory

val ids = intArrayOf(
    Npcs.GUARD_3269
)

val table = DropTableFactory
val droptable =
    table.build {
        guaranteed {
            obj(Items.BONES, quantity = 1)
        }
        main {
            total(512)
            obj(Items.STEEL_ARROW, quantityRange = 5..100, slots = 4)
            obj(Items.IRON_ARROW, quantityRange = 5..200, slots = 4)
            obj(Items.SHORTBOW, quantity = 1, slots = 4)
            obj(Items.OAK_SHORTBOW, quantity = 1, slots = 4)
            obj(Items.STEEL_SWORD, quantity = 1, slots = 4)
            obj(Items.COINS_995, quantity = 25, slots = 16)
            obj(Items.AIR_RUNE, quantityRange = 5..25, slots = 4)
            obj(Items.MIND_RUNE, quantityRange = 5..25, slots = 4)
            obj(Items.STEEL_SQ_SHIELD, quantity = 1, slots = 4)
            obj(Items.STEEL_MED_HELM, quantity = 1, slots = 4)
            obj(Items.STEEL_CHAINBODY, quantity = 1, slots = 4)
            obj(Items.STEEL_MACE, quantity = 1, slots = 4)
            obj(Items.STEEL_LONGSWORD, quantity = 1, slots = 4)
            obj(Items.STEEL_FULL_HELM, quantity = 1, slots = 2)
            obj(Items.STEEL_BATTLEAXE, quantity = 1, slots = 2)
            obj(Items.WATER_RUNE, quantityRange = 5..25, slots = 2)
            obj(Items.CHAOS_RUNE, quantityRange = 1..20, slots = 2)
            obj(Items.STEEL_KITESHIELD, quantity = 1, slots = 1)
            obj(Items.STEEL_PLATELEGS, quantity = 1, slots = 1)
            obj(Items.STEEL_PLATESKIRT, quantity = 1, slots = 1)
            obj(Items.STEEL_PLATEBODY, quantity = 1, slots = 1)
            obj(Items.STEEL_2H_SWORD, quantity = 1, slots = 1)
            obj(Items.STEEL_SCIMITAR, quantity = 1, slots = 1)
            nothing(slots = 64)
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
            attackSpeed = 4
            respawnDelay = 50
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 22
            attack = 19
            strength = 18
            defence = 14
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 18
            defenceSlash = 25
            defenceCrush = 19
            defenceMagic = -4
            defenceRanged = 20
        }


        anims {
            attack = Animation.HUMAN_SLASH_SWORD_ATTACK
            block = Animation.HUMAN_SHIELD_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_BLOCK_1
            deathSound = Sound.HUMAN_DEATH
        }
    }
}