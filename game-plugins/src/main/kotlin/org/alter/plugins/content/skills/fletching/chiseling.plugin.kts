package org.alter.plugins.content.skills.fletching

import org.alter.api.cfg.Items
import org.alter.plugins.content.skills.fletching.action.ChiselAction
import org.alter.plugins.content.skills.fletching.data.Chiseled

// ===========================================================================
// Whittling Logs

/**
 * De map van chiseled item‑ids naar hun definitie
 */
val chiseledDefs = Chiseled.chiseledDefinitions

/**
 * De Chiseling‑action
 */
val chiselAction = ChiselAction(world.definitions)

/**
 * Wanneer je chisel op een ongefrette item (gem of kebbit) gebruikt
 */
chiseledDefs.values.forEach { chiseled ->
    on_item_on_item(item1 = Items.CHISEL, item2 = chiseled.unchiseled) {
        makeChiseled(player, chiseled.id)
    }
}

/**
 * Opent de “hoeveel wil je chisel-en?” prompt
 */
fun makeChiseled(player: Player, chiseled: Int) {
    val def = chiseledDefs[chiseled] ?: return
    val max = player.inventory.getItemCount(def.unchiseled)
    when {
        max == 0   -> return
        max == 1   -> chisel(player, chiseled, 1)
        else       -> player.queue {
            produceItemBox(
                def.id,
                title = "How many would you like to chisel?",
                maxProducable = max,
                logic = ::chisel
            )
        }
    }
}

/**
 * Roept de daadwerkelijke chiseling‑action aan
 */
fun chisel(player: Player, item: Int, amount: Int) {
    val def = chiseledDefs[item] ?: return
    player.interruptQueues()
    player.resetInteractions()
    player.queue { chiselAction.chisel(this, def, amount) }
}
