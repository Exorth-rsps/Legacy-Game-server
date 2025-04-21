package org.alter.plugins.content.skills.crafting.data

import org.alter.api.cfg.Items

enum class Chiseled(val id: Int, val unchiseled: Int, val amount: Int = 12, val level: Int, val animation: Int, val craftingXP: Double) {
    AMETHYST_BOLT_TIPS(id = Items.AMETHYST_BOLT_TIPS, unchiseled = Items.AMETHYST, level = 83, amount = 15, animation = 890, craftingXP = 4.0),
    AMETHYST_ARROWTIPS(id = Items.AMETHYST_ARROWTIPS, unchiseled = Items.AMETHYST, level = 85, amount = 15, animation = 890, craftingXP = 4.0),
    AMETHYST_JAVELING_HEADS(id = Items.AMETHYST_JAVELIN_HEADS, unchiseled = Items.AMETHYST, level = 87, amount = 5, animation = 890, craftingXP = 6.0),
    AMETHYST_DART_TIP(id = Items.AMETHYST_DART_TIP, unchiseled = Items.AMETHYST, level = 89, amount = 8, animation = 890, craftingXP = 7.5),
    ;

    companion object {
        /**
         * The map of chiseled ids to its definition
         */
        val chiseledDefinitions = values().associate { it.id to it}
    }
}