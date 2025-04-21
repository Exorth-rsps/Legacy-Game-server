package org.alter.plugins.content.mechanics.boosts

import org.alter.api.Skills
import org.alter.game.model.timer.TimerKey
import org.alter.plugins.content.mechanics.prayer.Prayer
import org.alter.plugins.content.mechanics.prayer.Prayers

private val SKILL_RESTORE_TIMER = TimerKey()
private val HP_RESTORE_TIMER    = TimerKey()

private val DEFAULT_INTERVAL = 100
private val RAPID_INTERVAL   = 50

private val ALL_SKILL_IDS = listOf(
    Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE,
    Skills.HITPOINTS, Skills.RANGED, Skills.MAGIC,
    Skills.COOKING, Skills.WOODCUTTING, Skills.FLETCHING,
    Skills.FISHING, Skills.FIREMAKING, Skills.CRAFTING,
    Skills.SMITHING, Skills.MINING, Skills.HERBLORE,
    Skills.AGILITY, Skills.THIEVING, Skills.SLAYER,
    Skills.FARMING, Skills.RUNECRAFTING, Skills.HUNTER,
    Skills.CONSTRUCTION
)

on_login {
    player.timers[SKILL_RESTORE_TIMER] = DEFAULT_INTERVAL
    player.timers[HP_RESTORE_TIMER]    = DEFAULT_INTERVAL
}

on_timer(key = SKILL_RESTORE_TIMER) {
    val skillsComp = player.getSkills()
    ALL_SKILL_IDS
        .filter { it != Skills.HITPOINTS }
        .forEach { skillId ->
            val base    = skillsComp.getBaseLevel(skillId)
            val current = skillsComp.getCurrentLevel(skillId)
            when {
                current < base ->
                    skillsComp.incrementCurrentLevel(skillId, 1,  capped = true)   // nooit boven base
                current > base ->
                    skillsComp.incrementCurrentLevel(skillId, -1, capped = false)  // per stap omlaag
                else -> Unit
            }
        }

    val anyDrained = ALL_SKILL_IDS
        .filter { it != Skills.HITPOINTS }
        .any    { skillId ->
            val curr = skillsComp.getCurrentLevel(skillId)
            curr < skillsComp.getBaseLevel(skillId)
        }

    player.timers[SKILL_RESTORE_TIMER] =
        if (Prayers.isActive(player, Prayer.RAPID_RESTORE) && anyDrained) RAPID_INTERVAL
        else                                                      DEFAULT_INTERVAL
}

on_timer(key = HP_RESTORE_TIMER) {
    val skillsComp = player.getSkills()
    val base    = skillsComp.getBaseLevel(Skills.HITPOINTS)
    val current = skillsComp.getCurrentLevel(Skills.HITPOINTS)

    when {
        current < base ->
            skillsComp.incrementCurrentLevel(Skills.HITPOINTS, 1,  capped = true)
        current > base ->
            skillsComp.incrementCurrentLevel(Skills.HITPOINTS, -1, capped = false)
        else -> Unit
    }

    val hpDrained = current < base
    player.timers[HP_RESTORE_TIMER] =
        if (Prayers.isActive(player, Prayer.RAPID_HEAL) && hpDrained) RAPID_INTERVAL
        else                                                    DEFAULT_INTERVAL
}

on_player_death {
    player.timers[SKILL_RESTORE_TIMER] = DEFAULT_INTERVAL
    player.timers[HP_RESTORE_TIMER]    = DEFAULT_INTERVAL
}
