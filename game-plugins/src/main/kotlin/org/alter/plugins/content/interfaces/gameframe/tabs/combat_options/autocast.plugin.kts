package org.alter.plugins.content.interfaces.attack

import org.alter.plugins.content.interfaces.attack.AttackTab.SELECT_AUTOCAST_ATTR
import org.alter.api.InterfaceDestination
import org.alter.api.cfg.Varbit
import org.alter.plugins.content.combat.strategy.magic.CombatSpell
import org.alter.plugins.content.magic.MagicSpells

/**
 * Handles autocast spell selection.
 */

MagicSpells.getCombatSpells().forEach { (_, meta) ->
    on_button(interfaceId = meta.interfaceId, component = meta.component) {
        if (player.attr[SELECT_AUTOCAST_ATTR] != true) {
            return@on_button
        }
        val spell = CombatSpell.values.firstOrNull { it.id == meta.paramItem } ?: return@on_button
        player.setVarbit(Varbit.AUTOCAST_SPELL, spell.autoCastId)
        player.attr.remove(SELECT_AUTOCAST_ATTR)
        player.openInterface(InterfaceDestination.ATTACK)
    }
}

on_interface_close(interfaceId = InterfaceDestination.MAGIC.interfaceId) {
    if (player.attr.remove(SELECT_AUTOCAST_ATTR) == true) {
        player.openInterface(InterfaceDestination.ATTACK)
    }
}
