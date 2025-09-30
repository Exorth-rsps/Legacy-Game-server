package org.alter.plugins.content.combat.autocast

import org.alter.api.EquipmentType
import org.alter.api.WeaponType
import org.alter.api.ext.getVarbit
import org.alter.api.ext.hasEquipped
import org.alter.api.ext.hasWeaponType
import org.alter.api.ext.setVarbit
import org.alter.game.model.entity.Player
import org.alter.plugins.content.combat.Combat
import org.alter.plugins.content.combat.strategy.magic.CombatSpell

/**
 * Utility for validating and managing autocast spell selections.
 */
object Autocast {

    /** Returns true if the player's equipped weapon can autocast [spell]. */
    fun canAutocast(player: Player, spell: CombatSpell): Boolean {
        if (!player.hasWeaponType(WeaponType.MAGIC_STAFF)) {
            return false
        }
        val allowed = AutocastSpells.allowedStaves(spell) ?: return true
        return player.hasEquipped(EquipmentType.WEAPON, *allowed)
    }

    /** Clears any saved autocast state for [player]. */
    fun reset(player: Player) {
        player.attr.remove(Combat.CASTING_SPELL)
        player.setVarbit(Combat.SELECTED_AUTOCAST_VARBIT, 0)
        player.setVarbit(Combat.DEFENSIVE_MAGIC_CAST_VARBIT, 0)
    }

    /** Ensures the currently selected autocast spell is valid for the equipped weapon. */
    fun validate(player: Player) {
        val spell = AutocastSpells.forId(player.getVarbit(Combat.SELECTED_AUTOCAST_VARBIT)) ?: return
        if (!canAutocast(player, spell)) {
            reset(player)
        }
    }
}

