package org.alter.plugins.content.skills.crafting

import org.alter.api.cfg.Items
import org.alter.plugins.content.skills.crafting.action.ChiselAction
import org.alter.plugins.content.skills.crafting.data.Chiseled

// ===========================================================================
// Amethyst Gem Cutting (Crafting)

/**
 * De subset van chiseledDefinitions voor amethisten
 */
val amethystProducts = Chiseled.chiseledDefinitions
    .filter { it.value.unchiseled == Items.AMETHYST }
    .map { it.key }
    .toIntArray()

/**
 * De gem-cutting (chisel) actie
 */
val chiselAction = ChiselAction(world.definitions)

/**
 * Handler: gebruik chisel op ongesneden amethyst
 */
on_item_on_item(item1 = Items.CHISEL, item2 = Items.AMETHYST) {
    openAmethystBox(player)
}

/**
 * Opent de keuze‑prompt voor amethyst producten
 */
fun openAmethystBox(player: Player) {
    // bereken maximaal aantal (per stuk kost 1 amethist)
    val max = player.inventory.getItemCount(Items.AMETHYST)
    if (max == 0) return

    player.queue {
        produceItemBox(
            *amethystProducts,
            title         = "Which amethyst item would you like to make?",
            maxProducable = max,
            logic         = ::cutAmethyst
        )
    }
}

/**
 * Roept de chiseling actie voor amethisten aan
 */
fun cutAmethyst(player: Player, productId: Int, amount: Int) {
    // pak de definitie uit de crafting data map
    val def = Chiseled.chiseledDefinitions[productId] ?: return

    player.interruptQueues()
    player.resetInteractions()
    player.queue {
        chiselAction.chisel(this, def, amount)
    }
}
