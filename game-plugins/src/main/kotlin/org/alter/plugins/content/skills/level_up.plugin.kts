package org.alter.plugins.content.skills

import org.alter.api.cfg.Graphic
import org.alter.game.model.attr.LEVEL_UP_INCREMENT
import org.alter.game.model.attr.LEVEL_UP_SKILL_ID
import kotlin.random.Random


set_level_up_logic {
    val skill = player.attr[LEVEL_UP_SKILL_ID]!!
    val increment = player.attr[LEVEL_UP_INCREMENT]!!

    // Bestaande combat- en interface-logica
    if (Skills.isCombat(skill)) {
        player.calculateAndSetCombatLevel()
    }

    player.queue {
        if (player.getSkills()[skill].currentLevel == 99) {
            player.graphic(Graphic.FINAL_LEVEL_UP)
        } else {
            val levelupJingles = listOf(29, 67, 50)
            player.playJingle(levelupJingles[Random.nextInt(levelupJingles.size)])
            player.graphic(Graphic.LEVEL_UP, 124)
            player.message(
                "Congratulations, you've just advanced your ${
                    Skills.getSkillName(
                        world,
                        skill
                    )
                } level. You are now level ${player.getSkills().getBaseLevel(skill)}."
            )
            world.spawn(AreaSound(player.tile, 2396, 1, 1))
        }

        // Toon standaard level-up messagebox
        //levelUpMessageBox(skill, increment)

        // **Nieuw: globale broadcast bij level 90–99**
        val newLevel = player.getSkills().getBaseLevel(skill)
        if (newLevel in 90..99) {
            val skillName = Skills.getSkillName(world, skill)
            val playerName = player.username

            world.players.forEach { other ->
                other.message(
                    "<col=FF0000>[GLOBAL]</col> $playerName has reached level $newLevel in $skillName!",
                    ChatMessageType.CONSOLE
                )
            }
        }
    }
}
