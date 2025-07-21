package org.alter.plugins.content.interfaces.autocast

import org.alter.api.InterfaceDestination
import org.alter.api.EquipmentType
import org.alter.api.cfg.Varp
import org.alter.api.ext.*
import org.alter.plugins.content.combat.Combat
import org.alter.plugins.content.interfaces.attack.AttackTab

private const val AUTOCAST_INTERFACE_ID = 201

// Open autocast selection from the combat tab.
on_button(interfaceId = AttackTab.ATTACK_TAB_INTERFACE_ID, component = 19) {
    player.openInterface(interfaceId = AUTOCAST_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
}

// Example staff option to open the interface. More staffs can be added here.
on_equipment_option(item = 1381, option = "Choose spell") {
    player.openInterface(interfaceId = AUTOCAST_INTERFACE_ID, dest = InterfaceDestination.MAIN_SCREEN)
}

// Toggle defensive casting.
on_button(interfaceId = AUTOCAST_INTERFACE_ID, component = 5) {
    player.toggleVarbit(Combat.DEFENSIVE_MAGIC_CAST_VARBIT)
}

// Select a spell from the interface.
on_button(interfaceId = AUTOCAST_INTERFACE_ID, component = 4) {
    val slot = player.getInteractingSlot()
    player.setVarbit(Combat.SELECTED_AUTOCAST_VARBIT, slot + 1)
    player.getEquipment(EquipmentType.WEAPON.id)?.let { weapon ->
        player.setVarp(Varp.AUTOCAST_BASE_ITEM, weapon.id)
    }
}

// Reset autocast when the weapon is unequipped.
on_unequip_from_slot(EquipmentType.WEAPON.id) {
    player.setVarbit(Combat.SELECTED_AUTOCAST_VARBIT, 0)
}
