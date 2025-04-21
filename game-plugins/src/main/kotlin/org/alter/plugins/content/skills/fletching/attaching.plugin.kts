package org.alter.plugins.content.skills.fletching

import org.alter.api.cfg.Items
import org.alter.plugins.content.skills.fletching.action.AttachAction
import org.alter.plugins.content.skills.fletching.data.Attached

// ===========================================================================
// Attach items

/**
 * De map van Attached ids naar hun definitie
 */
val attachedDefs = Attached.attachedDefinitions

/**
 * De Attach actie
 */
val attachAction = AttachAction(world.definitions)

/**
 * Wanneer je het eerste materiaal op het tweede materiaal gebruikt
 */
attachedDefs.values.forEach { attached ->
    on_item_on_item(item1 = attached.firstMaterial, item2 = attached.secondMaterial) {
        makeAttached(player, attached.id)
    }
}

/**
 * Opent de "hoeveel wil je attach-en?" prompt
 */
fun makeAttached(player: Player, attachedId: Int) {
    val def = attachedDefs[attachedId] ?: return
    val maxAttachable = minOf(
        player.inventory.getItemCount(def.firstMaterial),
        player.inventory.getItemCount(def.secondMaterial)
    )
    when {
        maxAttachable == 0 -> return
        maxAttachable == 1 -> attach(player, attachedId, 1)
        else -> player.queue {
            produceItemBox(
                def.id,
                title = "How many would you like to attach?",
                maxProducable = maxAttachable,
                logic = ::attach
            )
        }
    }
}

/**
 * Roept de daadwerkelijke attach actie aan
 */
fun attach(player: Player, itemId: Int, amount: Int) {
    val def = attachedDefs[itemId] ?: return
    player.interruptQueues()
    player.resetInteractions()
    player.queue { attachAction.attach(this, def, amount) }
}