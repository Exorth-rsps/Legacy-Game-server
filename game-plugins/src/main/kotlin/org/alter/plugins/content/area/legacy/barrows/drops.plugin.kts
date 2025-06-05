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
        nothing(48)
    }
}

DropTableFactory.register(chestTable, Barrows.CHEST, type = DropTableType.CHEST)

private val brotherTables = arrayOf(
    DropTableFactory.build {
        main {
            total(128)
            obj(Items.DHAROKS_HELM)
            obj(Items.DHAROKS_GREATAXE)
            obj(Items.DHAROKS_PLATEBODY)
            obj(Items.DHAROKS_PLATELEGS)
            nothing(124)
        }
    },
    DropTableFactory.build {
        main {
            total(128)
            obj(Items.VERACS_HELM)
            obj(Items.VERACS_FLAIL)
            obj(Items.VERACS_BRASSARD)
            obj(Items.VERACS_PLATESKIRT)
            nothing(124)
        }
    },
    DropTableFactory.build {
        main {
            total(128)
            obj(Items.AHRIMS_HOOD)
            obj(Items.AHRIMS_STAFF)
            obj(Items.AHRIMS_ROBETOP)
            obj(Items.AHRIMS_ROBESKIRT)
            nothing(124)
        }
    },
    DropTableFactory.build {
        main {
            total(128)
            obj(Items.GUTHANS_HELM)
            obj(Items.GUTHANS_WARSPEAR)
            obj(Items.GUTHANS_PLATEBODY)
            obj(Items.GUTHANS_CHAINSKIRT)
            nothing(124)
        }
    },
    DropTableFactory.build {
        main {
            total(128)
            obj(Items.KARILS_COIF)
            obj(Items.KARILS_CROSSBOW)
            obj(Items.KARILS_LEATHERTOP)
            obj(Items.KARILS_LEATHERSKIRT)
            nothing(124)
        }
    },
    DropTableFactory.build {
        main {
            total(128)
            obj(Items.TORAGS_HELM)
            obj(Items.TORAGS_HAMMERS)
            obj(Items.TORAGS_PLATEBODY)
            obj(Items.TORAGS_PLATELEGS)
            nothing(124)
        }
    }
)

Barrows.BROTHERS.forEachIndexed { idx, brother ->
    DropTableFactory.register(brotherTables[idx], brother.id, type = DropTableType.CHEST)
}

val BROTHER_TABLE_IDS = Barrows.BROTHERS.map { it.id }.toIntArray()
