package org.alter.plugins.content.drops.global

import org.alter.api.cfg.Items
import org.alter.plugins.content.drops.DropTableFactory

object Herbs {
    val minorHerbTable =
        DropTableFactory.build {
            main {
                total(1024)
                obj(Items.GRIMY_MARRENTILL, quantity = 1, slots = 198)
                obj(Items.GRIMY_TARROMIN, quantity = 1, slots = 146)
                obj(Items.GRIMY_HARRALANDER, quantity = 1, slots = 114)
                obj(Items.GRIMY_AVANTOE, quantity = 1, slots = 49)
                obj(Items.GRIMY_KWUARM, quantity = 1, slots = 39)
                obj(Items.GRIMY_CADANTINE, quantity = 1, slots = 32)
                obj(Items.GRIMY_LANTADYME, quantity = 1, slots = 24)
                obj(Items.GRIMY_DWARF_WEED, quantity = 1, slots = 24)
            }
        }

    val minorMultipleHerbTable =
        DropTableFactory.build {
            main {
                total(26)
                table(minorHerbTable1, slots = 11)
                table(minorHerbTable2, slots = 11)
                table(minorHerbTable3, slots = 4)
            }
        }

    private val minorHerbTable1 =
        DropTableFactory.build {
            main {
                total(1024)
                obj(Items.GRIMY_MARRENTILL_NOTED, quantity = 1, slots = 198)
                obj(Items.GRIMY_TARROMIN_NOTED, quantity = 1, slots = 146)
                obj(Items.GRIMY_HARRALANDER_NOTED, quantity = 1, slots = 114)
                obj(Items.GRIMY_AVANTOE_NOTED, quantity = 1, slots = 49)
                obj(Items.GRIMY_KWUARM_NOTED, quantity = 1, slots = 39)
                obj(Items.GRIMY_CADANTINE_NOTED, quantity = 1, slots = 32)
                obj(Items.GRIMY_LANTADYME_NOTED, quantity = 1, slots = 24)
                obj(Items.GRIMY_DWARF_WEED_NOTED, quantity = 1, slots = 24)
            }
        }

    private val minorHerbTable2 =
        DropTableFactory.build {
            main {
                total(1024)
                obj(Items.GRIMY_MARRENTILL_NOTED, quantity = 2, slots = 198)
                obj(Items.GRIMY_TARROMIN_NOTED, quantity = 2, slots = 146)
                obj(Items.GRIMY_HARRALANDER_NOTED, quantity = 2, slots = 114)
                obj(Items.GRIMY_AVANTOE_NOTED, quantity = 2, slots = 49)
                obj(Items.GRIMY_KWUARM_NOTED, quantity = 2, slots = 39)
                obj(Items.GRIMY_CADANTINE_NOTED, quantity = 2, slots = 32)
                obj(Items.GRIMY_LANTADYME_NOTED, quantity = 2, slots = 24)
                obj(Items.GRIMY_DWARF_WEED_NOTED, quantity = 2, slots = 24)
            }
        }

    private val minorHerbTable3 =
        DropTableFactory.build {
            main {
                total(1024)
                obj(Items.GRIMY_MARRENTILL_NOTED, quantity = 3, slots = 198)
                obj(Items.GRIMY_TARROMIN_NOTED, quantity = 3, slots = 146)
                obj(Items.GRIMY_HARRALANDER_NOTED, quantity = 3, slots = 114)
                obj(Items.GRIMY_AVANTOE_NOTED, quantity = 3, slots = 49)
                obj(Items.GRIMY_KWUARM_NOTED, quantity = 3, slots = 39)
                obj(Items.GRIMY_CADANTINE_NOTED, quantity = 3, slots = 32)
                obj(Items.GRIMY_LANTADYME_NOTED, quantity = 3, slots = 24)
                obj(Items.GRIMY_DWARF_WEED_NOTED, quantity = 3, slots = 24)
            }
        }

    val usefulHerbTable =
        DropTableFactory.build {
            main {
                total(1024)
                obj(Items.GRIMY_AVANTOE, quantity = 1, slots = 341)
                obj(Items.GRIMY_SNAPDRAGON, quantity = 1, slots = 239)
                obj(Items.GRIMY_TORSTOL, quantity = 1, slots = 205)
            }
        }
}
