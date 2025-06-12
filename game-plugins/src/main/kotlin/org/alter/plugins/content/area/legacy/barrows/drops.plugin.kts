package org.alter.plugins.content.area.legacy.barrows

import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs
import org.alter.plugins.content.drops.DropTableFactory
import org.alter.plugins.content.drops.DropTableType

val chestTable = DropTableFactory.build {
    main {
        total(128)
        obj(Items.DEATH_RUNE, quantityRange = 30..100, slots = 16)
        obj(Items.BLOOD_RUNE, quantityRange = 30..100, slots = 16)
        obj(Items.CHAOS_RUNE, quantityRange = 50..150, slots = 16)
        obj(Items.MIND_RUNE, quantityRange = 50..150, slots = 16)
        obj(Items.BOLT_RACK, quantityRange = 50..100, slots = 16)
        obj(Items.COINS_995, quantityRange = 1000..5000, slots = 16)
    }
}

DropTableFactory.register(chestTable, Barrows.CHEST, type = DropTableType.CHEST)

private val brotherTables = arrayOf(
    DropTableFactory.build {
        main {
            total(128)

            obj(Items.DHAROKS_HELM, quantity = 1, slots = 1)
            obj(Items.DHAROKS_GREATAXE, quantity = 1, slots = 1)
            obj(Items.DHAROKS_PLATEBODY, quantity = 1, slots = 1)
            obj(Items.DHAROKS_PLATELEGS, quantity = 1, slots = 1)
            nothing(124)
        }
    },
    DropTableFactory.build {
        main {
            total(128)
            obj(Items.VERACS_HELM, quantity = 1, slots = 1)
            obj(Items.VERACS_FLAIL, quantity = 1, slots = 1)
            obj(Items.VERACS_BRASSARD, quantity = 1, slots = 1)
            obj(Items.VERACS_PLATESKIRT, quantity = 1, slots = 1)
            nothing(124)
        }
    },
    DropTableFactory.build {
        main {
            total(128)

            obj(Items.AHRIMS_HOOD, quantity = 1, slots = 1)
            obj(Items.AHRIMS_STAFF, quantity = 1, slots = 1)
            obj(Items.AHRIMS_ROBETOP, quantity = 1, slots = 1)
            obj(Items.AHRIMS_ROBESKIRT, quantity = 1, slots = 1)
            nothing(124)
        }
    },
    DropTableFactory.build {
        main {
            total(128)
            obj(Items.GUTHANS_HELM, quantity = 1, slots = 1)
            obj(Items.GUTHANS_WARSPEAR, quantity = 1, slots = 1)
            obj(Items.GUTHANS_PLATEBODY, quantity = 1, slots = 1)
            obj(Items.GUTHANS_CHAINSKIRT, quantity = 1, slots = 1)
            nothing(124)
        }
    },
    DropTableFactory.build {
        main {
            total(128)
            obj(Items.KARILS_COIF, quantity = 1, slots = 1)
            obj(Items.KARILS_CROSSBOW, quantity = 1, slots = 1)
            obj(Items.KARILS_LEATHERTOP, quantity = 1, slots = 1)
            obj(Items.KARILS_LEATHERSKIRT, quantity = 1, slots = 1)
            nothing(124)
        }
    },
    DropTableFactory.build {
        main {
            total(128)
            obj(Items.TORAGS_HELM, quantity = 1, slots = 1)
            obj(Items.TORAGS_HAMMERS, quantity = 1, slots = 1)
            obj(Items.TORAGS_PLATEBODY, quantity = 1, slots = 1)
            obj(Items.TORAGS_PLATELEGS, quantity = 1, slots = 1)
            nothing(124)
        }
    }
)

Barrows.BROTHERS.forEachIndexed { idx, brother ->
    DropTableFactory.register(brotherTables[idx], brother.id, type = DropTableType.CHEST)
}

val BROTHER_TABLE_IDS = Barrows.BROTHERS.map { it.id }.toIntArray()
