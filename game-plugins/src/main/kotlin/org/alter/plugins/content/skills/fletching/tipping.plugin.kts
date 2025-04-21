package org.alter.plugins.content.skills.fletching

import org.alter.api.cfg.Items
import org.alter.plugins.content.skills.fletching.data.Tipped
import org.alter.plugins.content.skills.fletching.action.TipAction

// ===========================================================================
// Tipping Ammunition

/**
 * De map van Tipped ids naar hun definitie
 */
val tippedDefs = Tipped.tippedDefinitions

/**
 * De tipping action
 */
val tipAction = TipAction(world.definitions)

/**
 * Wanneer je ongepunt munitie op een tip gebruikt
 */
tippedDefs.values.forEach { tipped ->
    on_item_on_item(item1 = tipped.base, item2 = tipped.tip) {
        makeTipped(player, tipped.id)
    }
}

/**
 * Opent de "hoeveel sets wil je tippen?" prompt
 */
fun makeTipped(player: Player, tippedId: Int) {
    val def = tippedDefs[tippedId] ?: return
    val maxSets = Math.ceil(
        minOf(
            player.inventory.getItemCount(def.base),
            player.inventory.getItemCount(def.tip)
        ).toDouble() / def.setAmount
    ).toInt()
    when {
        maxSets == 0 -> return
        maxSets == 1 -> tip(player, tippedId, 1)
        else -> player.queue {
            produceItemBox(
                def.id,
                title         = "How many sets would you like to tip?",
                maxProducable = maxSets,
                logic         = ::tip
            )
        }
    }
}

/**
 * Roept de tipping actie aan met de gekozen hoeveelheid
 */
fun tip(player: Player, itemId: Int, amount: Int) {
    val def = tippedDefs[itemId] ?: return
    player.interruptQueues()
    player.resetInteractions()
    player.queue {
        tipAction.tip(this, def, amount)
    }
}
