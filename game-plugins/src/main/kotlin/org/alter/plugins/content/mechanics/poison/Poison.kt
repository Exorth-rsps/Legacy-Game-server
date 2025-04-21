package org.alter.plugins.content.mechanics.poison

import org.alter.game.model.attr.POISON_TICKS_LEFT_ATTR
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.model.timer.POISON_TIMER
import org.alter.api.EquipmentType
import org.alter.api.cfg.Items
import org.alter.api.ext.hasEquipped
import org.alter.api.ext.setVarp

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Poison {

    private const val HP_ORB_VARP = 102

    fun getDamageForTicks(ticks: Int) = (ticks / 5) + 1

    fun isImmune(pawn: Pawn): Boolean = when (pawn) {
        is Player -> pawn.hasEquipped(EquipmentType.HEAD, Items.SERPENTINE_HELM, Items.TANZANITE_HELM, Items.MAGMA_HELM)
        is Npc    -> pawn.combatDef.poisonImmunity
        else      -> false
    }

    fun poison(pawn: Pawn, initialDamage: Int): Boolean {
        val ticks = (initialDamage * 5) - 4
        val oldDamage = getDamageForTicks(pawn.attr[POISON_TICKS_LEFT_ATTR] ?: 0)
        val newDamage = getDamageForTicks(ticks)
        if (oldDamage > newDamage) {
            return false
        }
        pawn.timers[POISON_TIMER] = 1
        pawn.attr[POISON_TICKS_LEFT_ATTR] = ticks
        return true
    }

    fun setHpOrb(player: Player, state: OrbState) {
        val value = when (state) {
            OrbState.NONE   -> 0
            OrbState.POISON -> 1
            OrbState.VENOM  -> 1_000_000
        }
        player.setVarp(HP_ORB_VARP, value)
    }

    /**
     * Stopt alle poison‐effecten op de speler (of NPC), zet de varp terug
     * naar normaal en verwijdert eventuele ticks.
     */
    fun cureWithPotion(pawn: Pawn, itemId: Int) {
        // 1) Stop poison‐timer
        pawn.timers.remove(POISON_TIMER)
        // 2) Reset resterende poison‐ticks
        pawn.attr.remove(POISON_TICKS_LEFT_ATTR)
        // 3) Voor spelers: zet de HP‐orb weer op NONE
        if (pawn is Player) {
            setHpOrb(pawn, OrbState.NONE)
        }
    }

    enum class OrbState {
        NONE,
        POISON,
        VENOM
    }
}
