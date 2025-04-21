package org.alter.plugins.content.skills.fletching

import org.alter.api.cfg.Items
import org.alter.game.model.attr.INTERACTING_ITEM_ID
import org.alter.game.model.attr.OTHER_ITEM_ID_ATTR
import org.alter.plugins.content.skills.fletching.action.FeatherAction
import org.alter.plugins.content.skills.fletching.data.Feathered

// ===========================================================================
// Feathering Darts/Bolts

/**
 * De map van Feathered item ids naar hun definitie
 */
val featheredDefs = Feathered.featheredDefinitions

/**
 * Lijst van alle mogelijke veren
 */
val feathers = Feathered.feathers

/**
 * De Feather actie
 */
val featherAction = FeatherAction(world.definitions)

/**
 * Wanneer je een onafgewerkt item op een veer gebruikt
 */
featheredDefs.values.forEach { feathered ->
    feathers.forEach { feather ->
        if (feathered.id == Items.HEADLESS_ARROW || feathered.id == Items.FLIGHTED_OGRE_ARROW) {
            on_item_on_item(item1 = feathered.unfeathered, item2 = feather) {
                featherShaft(player, feathered.id)
            }
        } else {
            on_item_on_item(item1 = feathered.unfeathered, item2 = feather) {
                feather(player, feathered.id)
            }
        }
    }
}

/**
 * Speciaal geval voor shaft-feathering: vraagt aantal sets
 */
fun featherShaft(player: Player, featheredId: Int) {
    // Haal de gekozen veer op en forceer niet-null
    val interacting  = player.attr[INTERACTING_ITEM_ID]
    val other        = player.attr[OTHER_ITEM_ID_ATTR]

    val feather: Int = when {
        interacting != null && feathers.contains(interacting) -> interacting
        other       != null && feathers.contains(other)       -> other
        else -> return
    }

    val def = featheredDefs[featheredId] ?: return

    // feather is nu gegarandeerd een Int, geen Int?
    val unfeatheredCount = player.inventory.getItemCount(def.unfeathered)
    val featherCount     = player.inventory.getItemCount(feather)
    val setsPossible     = featherCount / def.feathersNeeded

    val maxSets = Math.ceil(
        minOf(unfeatheredCount, setsPossible).toDouble() / def.amount
    ).toInt()

    when {
        maxSets == 0 -> return
        maxSets == 1 -> feather(player, featheredId)
        else         -> player.queue {
            produceItemBox(
                def.id,
                title         = "How many sets would you like to feather?",
                maxProducable = maxSets,
                logic         = ::feather
            )
        }
    }
}


/**
 * Algemene feathering actie met optionele hoeveelheid
 */
fun feather(player: Player, featheredId: Int, amount: Int = 1) {
    // Haal de attr‑waarden op
    val interacting = player.attr[INTERACTING_ITEM_ID]
    val other       = player.attr[OTHER_ITEM_ID_ATTR]

    // Bepaal welke veer het is, en forceer non-null
    val feather: Int = when {
        interacting != null && feathers.contains(interacting) -> interacting
        other       != null && feathers.contains(other)       -> other
        else -> return
    }

    val def = featheredDefs[featheredId] ?: return

    player.interruptQueues()
    player.resetInteractions()
    player.queue {
        // feather is nu gegarandeerd Int, geen Int?
        featherAction.feather(this, def, feather, amount)
    }
}
