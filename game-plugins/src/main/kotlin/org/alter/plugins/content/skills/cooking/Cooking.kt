package org.alter.plugins.content.skills.cooking

import org.alter.api.cfg.Items
import org.alter.game.fs.DefinitionSet
import org.alter.game.fs.def.ItemDef
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.api.Skills
import org.alter.api.ext.*
/*import org.alter.plugins.content.modules.tutorialisland.TutorialIsland
import org.alter.plugins.content.modules.tutorialisland.events.CookedBreadEvent
import org.alter.plugins.content.modules.tutorialisland.events.CookedShrimpEvent
import org.alter.plugins.content.modules.tutorialisland.events.CreateBreadDoughEvent*/
import org.alter.plugins.content.skills.cooking.data.CookingFood
import org.alter.plugins.content.skills.cooking.data.CookingIngredient
import org.alter.plugins.content.skills.cooking.data.CookingObj

class Cooking(private val defs: DefinitionSet) {

    val foodNames = CookingFood.values.associate { it.raw_item to defs.get(ItemDef::class.java, it.raw_item).name.lowercase() }
    val cookedFoodNames = CookingFood.values.associate { it.cooked_item to defs.get(ItemDef::class.java, it.cooked_item).name.lowercase() }

    val ingredientNames = CookingIngredient.values.associate { it.result to defs.get(ItemDef::class.java, it.result).name.lowercase() }

    suspend fun cook(task: QueueTask, food: CookingFood, amount: Int, obj: CookingObj, forceBurn: Boolean = false) {
        val player = task.player

        val name = foodNames[food.raw_item] ?: return
        val burnName = cookedFoodNames[food.cooked_item] ?: return
        var cookable: Int

        if(forceBurn) {
            cookable = food.cooked_item
        } else {
            cookable = food.raw_item
        }

        var animationPlaying = false
        for (i in 0 until amount) {
            if (!canCook(player, food, forceBurn)) {
                break
            }

            player.animate(obj.animation)
            animationPlaying = true
            player.playSound(obj.sound, 1, 0)

            player.inventory.remove(cookable)
            val level = player.getSkills().getCurrentLevel(Skills.COOKING)
            if(forceBurn) {
                player.inventory.add(food.burnt_item)
                player.filterableMessage("You deliberately burn some ${burnName}.")
            }
            else if(successfullyCooked(player, level, food, obj)) /*|| player.getVarp(TutorialIsland.COMPLETION_VARP) == 90 || player.getVarp(TutorialIsland.COMPLETION_VARP) == 160)*/ {
                player.inventory.add(food.cooked_item, 1)
                player.addXp(Skills.COOKING, food.xp)
                player.filterableMessage("You cook some ${name}.")
                /*if(player.getVarp(TutorialIsland.COMPLETION_VARP) == 90) {
                    player.world.plugins.executeEvent(player, CookedShrimpEvent)
                } else if(player.getVarp(TutorialIsland.COMPLETION_VARP) == 160) {
                  player.world.plugins.executeEvent(player, CookedBreadEvent)
                } else {
                    player.filterableMessage("You cook some ${name}.")
                }*/

            } else {
                player.inventory.add(food.burnt_item, 1)
                player.filterableMessage("You accidentally burn some ${name}.")
            }
            task.wait(5)
        }

        if (animationPlaying) {
            player.animate(-1)
        }
    }

    private fun canCook(player: Player, food: CookingFood, forceBurn: Boolean): Boolean {
        var name: String
        var cookable: Int
        if(forceBurn) {
            name = cookedFoodNames[food.cooked_item] ?: return false
            cookable = food.cooked_item
        } else {
            name = foodNames[food.raw_item] ?: return false
            cookable = food.raw_item
        }

        if(!player.inventory.contains(cookable)) {
            if(!forceBurn) {
                player.filterableMessage("You don't have any more $name to cook.")
            } else {
                player.filterableMessage("You don't have any more $name to burn.")
            }
            return false
        }

        if(player.getSkills().getCurrentLevel(Skills.COOKING) < food.minLevel && !forceBurn) {
            player.filterableMessage("You need a ${Skills.getSkillName(player.world, Skills.COOKING)} level of at least ${food.minLevel} to cook ${name}.")
            return false
        }
        return true
    }

    private fun successfullyCooked(player: Player, level: Int, food: CookingFood, obj: CookingObj): Boolean {
        val startOffset = if (obj.isRange) RANGE_START_OFFSET else FIRE_START_OFFSET
        val baseStop = food.stopLevel + STOP_EXTRA_LEVELS
        val stopLevel = if (player.equipment.contains(Items.COOKING_GAUNTLETS)) {
            (food.gauntletStopLevel ?: food.stopLevel) + STOP_EXTRA_LEVELS
        } else {
            baseStop
        }

        val burnStart = food.minLevel.toDouble() - startOffset

        val burnSpan = (stopLevel - burnStart).coerceAtLeast(1.0)
        val effectiveLevel = level.toDouble()

        // The OSRS wiki models burn reduction as a linear drop-off that starts roughly
        // two dozen levels below the requirement on fires and about thirty levels below
        // on ranges. Shifting the start of the curve by those amounts (and extending the
        // stop-burn level a few virtual levels beyond the documented cap) reproduces the
        // published success rates for high-level fish such as sharks while letting food
        // definitions specify alternate gauntlet-assisted burn caps.
        val chance = ((effectiveLevel - burnStart).coerceAtLeast(0.0) / burnSpan).coerceIn(0.0, 1.0)
        return RANDOM.nextDouble() <= chance
    }

    companion object {
        private const val RANGE_START_OFFSET = 32.0
        private const val FIRE_START_OFFSET = 24.0
        private const val STOP_EXTRA_LEVELS = 3.0
    }

    suspend fun combine(task: QueueTask, ingredient: CookingIngredient, amount: Int) {
        val player = task.player

        val name = ingredientNames[ingredient.result] ?: return

        repeat(amount) {

            if(!canCombine(player, ingredient)) {
                return
            }

            player.inventory.remove(ingredient.item1)
            player.inventory.remove(ingredient.item2)

            if(ingredient.usedItem1 != -1) {
                player.inventory.add(ingredient.usedItem1,1)
            }
            if(ingredient.usedItem2 != -1) {
                player.inventory.add(ingredient.usedItem2, 1)
            }

            player.inventory.add(ingredient.result, 1)
            if(ingredient.xp > 0.0) {
                player.addXp(Skills.COOKING, ingredient.xp)
            }

            /*if(player.getVarp(TutorialIsland.COMPLETION_VARP) == 150) {
                player.world.plugins.executeEvent(player, CreateBreadDoughEvent)
            } else {
                player.filterableMessage("You combine the ingredients to make $name.")
            }*/
            player.filterableMessage("You combine the ingredients to make $name.")
            task.wait(cycles = 3)
        }
    }


    private fun canCombine(player: Player, ingredient: CookingIngredient): Boolean {
        val name = ingredientNames[ingredient.result] ?: return false
        if(!player.inventory.contains(ingredient.item1) || !player.inventory.contains(ingredient.item2)) {
            player.filterableMessage("You are missing the required ingredients to make $name.")
            /*if(player.getVarp(TutorialIsland.COMPLETION_VARP) >= 1000) {
                player.filterableMessage("You are missing the required ingredients to make $name.")
            }*/
            return false
        }

        if(player.getSkills().getCurrentLevel(Skills.COOKING) < ingredient.minLevel) {
            player.filterableMessage("You must have a ${Skills.getSkillName(player.world, Skills.COOKING)} level of ${ingredient.minLevel} to make $name.")
            return false
        }

        if(player.inventory.freeSlotCount == 0) {
            player.filterableMessage("You don't have enough inventory space to make $name.")
            return false
        }
        return true
    }
}