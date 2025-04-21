package org.alter.plugins.content.skills.fletching

import org.alter.api.cfg.Items
import org.alter.game.model.attr.INTERACTING_ITEM_ID
import org.alter.game.model.attr.OTHER_ITEM_ID_ATTR
import org.alter.plugins.content.skills.fletching.action.WhittlingAction
import org.alter.plugins.content.skills.fletching.data.Log

// ===========================================================================
// Whittling Logs

/**
 * De map van Log ids naar hun definitie
 */
val logDefs = Log.logDefinitions

/**
 * De Whittling actie
 */
val whittleAction = WhittlingAction(world.definitions)

/**
 * Wanneer je mes gebruikt op een log
 */
logDefs.keys.forEach { logId ->
    on_item_on_item(item1 = Items.KNIFE, item2 = logId) {
        cutLog(player, logId)
    }
}

/**
 * Opent de keuzedialoog voor welk item je van de log wilt maken
 */
fun cutLog(player: Player, logId: Int) {
    val choices = logDefs[logId]?.values?.map { it.id }?.toIntArray() ?: return
    if (choices.isEmpty()) return
    player.queue {
        produceItemBox(
            *choices,
            title = "What would you like to make?",
            logic = ::whittle
        )
    }
}

/**
 * Roept de whittle-actie aan met de gekozen hoeveelheid
 */
fun whittle(player: Player, itemId: Int, amount: Int) {
    // probeer eerst INTERACTING_ITEM_ID, en anders OTHER_ITEM_ID_ATTR
    val interactingId = player.attr[INTERACTING_ITEM_ID]
    val otherId       = player.attr[OTHER_ITEM_ID_ATTR]

    val logId: Int = when {
        interactingId != null && logDefs.containsKey(interactingId) -> interactingId
        otherId       != null && logDefs.containsKey(otherId)       -> otherId
        else -> return   // geen geldige log, dus out
    }

    val option = logDefs[logId]?.get(itemId) ?: return

    player.interruptQueues()
    player.resetInteractions()
    player.queue {
        whittleAction.whittle(this, logId, option, amount)
    }
}
