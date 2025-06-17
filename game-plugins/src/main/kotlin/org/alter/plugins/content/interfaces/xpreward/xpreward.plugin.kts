package org.alter.plugins.content.interfaces.xpreward
import org.alter.game.model.attr.XP_REWARD_ITEM
import org.alter.game.model.attr.XP_REWARD_SKILL
import org.alter.api.ext.message
import org.alter.api.ext.closeInterface
import org.alter.api.ext.playSound
import org.alter.api.ext.setComponentText
import org.alter.api.Skills
import org.alter.api.cfg.Sound

XpReward.COMPONENT_TO_SKILL.forEach { (component, skill) ->
    on_button(interfaceId = XpReward.INTERFACE_ID, component = component) {
        player.attr[XP_REWARD_SKILL] = skill
        val skillName = Skills.getSkillName(player.world, skill)
        player.setComponentText(XpReward.INTERFACE_ID, XpReward.CONFIRM_COMPONENT, "Confirm: $skillName")
        player.playSound(Sound.INTERFACE_SELECT1)
    }
}

on_button(interfaceId = XpReward.INTERFACE_ID, component = 1) {
    player.closeInterface(XpReward.INTERFACE_ID)
}

on_button(interfaceId = XpReward.INTERFACE_ID, component = XpReward.CONFIRM_COMPONENT) {
    val item = player.attr[XP_REWARD_ITEM] ?: return@on_button
    val skill = player.attr[XP_REWARD_SKILL]
    if (skill == null || skill == -1) {
        player.message("You need to choose which skill you wish to be advanced.")
        return@on_button
    }
    player.addXp(skill, 150.0)
    player.inventory.remove(item)
    player.message("You feel more experienced.")
    player.closeInterface(XpReward.INTERFACE_ID)
}

on_interface_close(XpReward.INTERFACE_ID) {
    player.attr.remove(XP_REWARD_ITEM)
    player.attr.remove(XP_REWARD_SKILL)
}
