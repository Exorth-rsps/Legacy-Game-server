package org.alter.plugins.content.mechanics.random_events

import org.alter.api.Skills
import org.alter.api.cfg.Items
import org.alter.game.model.queue.QueueTask

on_item_option(item = Items.LAMP, option = "Rub") {
    player.queue { chooseSkillXp(Items.LAMP) }
}

on_item_option(item = Items.BOOK_OF_KNOWLEDGE, option = "Read") {
    player.queue { chooseSkillXp(Items.BOOK_OF_KNOWLEDGE) }
}

private val SKILLS = intArrayOf(
    Skills.ATTACK,
    Skills.STRENGTH,
    Skills.DEFENCE,
    Skills.RANGED,
    Skills.PRAYER,
    Skills.MAGIC,
    Skills.RUNECRAFTING,
    Skills.CONSTRUCTION,
    Skills.HITPOINTS,
    Skills.AGILITY,
    Skills.HERBLORE,
    Skills.THIEVING,
    Skills.CRAFTING,
    Skills.FLETCHING,
    Skills.SLAYER,
    Skills.HUNTER,
    Skills.MINING,
    Skills.SMITHING,
    Skills.FISHING,
    Skills.COOKING,
    Skills.FIREMAKING,
    Skills.WOODCUTTING,
    Skills.FARMING
)

private val SKILL_NAMES = arrayOf(
    "Attack",
    "Strength",
    "Defence",
    "Ranged",
    "Prayer",
    "Magic",
    "Runecrafting",
    "Construction",
    "Hitpoints",
    "Agility",
    "Herblore",
    "Thieving",
    "Crafting",
    "Fletching",
    "Slayer",
    "Hunter",
    "Mining",
    "Smithing",
    "Fishing",
    "Cooking",
    "Firemaking",
    "Woodcutting",
    "Farming"
)

suspend fun QueueTask.chooseSkillXp(item: Int) {
    var index = 0
    while (true) {
        val remaining = SKILLS.size - index
        val opts = mutableListOf<String>()
        for (i in 0 until minOf(5, remaining)) {
            opts += SKILL_NAMES[index + i]
        }
        if (index + 5 < SKILLS.size) {
            opts += "More"
        } else {
            opts += "Cancel"
        }
        val selection = options(*opts.toTypedArray())
        val maxIndex = minOf(5, remaining)
        when (selection) {
            in 1..maxIndex -> {
                val skill = SKILLS[index + selection - 1]
                player.addXp(skill, 150.0)
                player.inventory.remove(item)
                player.message("You feel more experienced.")
                return
            }
            maxIndex + 1 -> {
                if (index + 5 < SKILLS.size) {
                    index += 5
                } else {
                    player.message("You decide not to use it right now.")
                    return
                }
            }
        }
    }
}
