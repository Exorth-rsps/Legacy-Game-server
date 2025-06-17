package org.alter.plugins.content.interfaces.xpreward

import org.alter.api.InterfaceDestination
import org.alter.api.Skills
import org.alter.api.ext.InterfaceEvent
import org.alter.api.ext.openInterface
import org.alter.api.ext.setInterfaceEvents
import org.alter.api.ext.setInterfaceUnderlay
import org.alter.game.model.entity.Player
import org.alter.game.model.attr.XP_REWARD_ITEM
import org.alter.game.model.attr.XP_REWARD_SKILL

object XpReward {
    const val INTERFACE_ID = 240
    const val CONFIRM_COMPONENT = 26

    /**
     * Mapping of interface component ids to the skill they represent.
     * Annotated with [JvmField] so scripts can access the map directly
     * without going through the generated getter.
     */
    @JvmField
    val COMPONENT_TO_SKILL = mapOf(
        2 to Skills.ATTACK,
        3 to Skills.STRENGTH,
        4 to Skills.RANGED,
        5 to Skills.MAGIC,
        6 to Skills.DEFENCE,
        7 to Skills.HITPOINTS,
        8 to Skills.PRAYER,
        9 to Skills.AGILITY,
        10 to Skills.HERBLORE,
        11 to Skills.THIEVING,
        12 to Skills.CRAFTING,
        13 to Skills.RUNECRAFTING,
        14 to Skills.SLAYER,
        15 to Skills.FARMING,
        16 to Skills.MINING,
        17 to Skills.SMITHING,
        18 to Skills.FISHING,
        19 to Skills.COOKING,
        20 to Skills.FIREMAKING,
        21 to Skills.WOODCUTTING,
        22 to Skills.FLETCHING,
        23 to Skills.CONSTRUCTION,
        24 to Skills.HUNTER
    )

    fun open(p: Player, item: Int) {
        p.attr[XP_REWARD_ITEM] = item
        p.attr[XP_REWARD_SKILL] = -1
        p.setInterfaceUnderlay(-1, -1)
        p.openInterface(INTERFACE_ID, InterfaceDestination.MAIN_SCREEN)
        p.setComponentText(INTERFACE_ID, 0, "Choose the stat you wish to be advanced!")
        COMPONENT_TO_SKILL.keys.forEach { comp ->
            p.setInterfaceEvents(INTERFACE_ID, comp, 1..1, InterfaceEvent.ClickOp1)
        }
        p.setInterfaceEvents(INTERFACE_ID, 1, 1..1, InterfaceEvent.ClickOp1)
        p.setInterfaceEvents(INTERFACE_ID, CONFIRM_COMPONENT, 1..1, InterfaceEvent.ClickOp1)
    }
}
