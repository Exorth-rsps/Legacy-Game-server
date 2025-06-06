package org.alter.plugins.content.skills.woodcutting

import org.alter.game.fs.def.ItemDef
import org.alter.game.model.attr.AttributeKey
import org.alter.game.model.entity.DynamicObject
import org.alter.game.model.entity.GameObject
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.api.Skills
import org.alter.api.cfg.Items

import org.alter.api.ext.*
import kotlin.random.Random

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Woodcutting {

    val infernalAxe = AttributeKey<Int>("Infernal Axe Charges")

    data class Tree(val type: TreeType, val obj: Int, val trunk: Int)

    suspend fun chopDownTree(it: QueueTask, obj: GameObject, tree: TreeType, trunkId: Int) {
        val p = it.player

        if (!canChop(p, obj, tree)) {
            return
        }

        val logName = p.world.definitions.get(ItemDef::class.java, tree.log).name
        // Kies de beste beschikbare axe in inventory of equipment
        val axe = AxeType.values
            .filter { p.getSkills().getBaseLevel(Skills.WOODCUTTING) >= it.level
                    && (p.equipment.contains(it.item) || p.inventory.contains(it.item)) }
            .maxByOrNull { it.level }
            ?: run {
                p.message("You need an axe to chop down this tree.")
                return
            }

        p.filterableMessage("You swing your axe at the tree.")
        while (true) {
            p.animate(axe.animation)
            it.wait(2)

            if (!canChop(p, obj, tree)) {
                p.animate(-1)
                break
            }

            val level = p.getSkills().getCurrentLevel(Skills.WOODCUTTING)
            if (level.interpolate(minChance = 60, maxChance = 190, minLvl = 1, maxLvl = 99, cap = 255)) {
                p.filterableMessage("You get some ${logName.pluralSuffix(2)}.")
                p.playSound(3600)
                p.inventory.add(tree.log)
                if (Random.nextInt(5) == 0) {
                    val bonusItemId = Items.ANIMAINFUSED_BARK
                    val maxAmount = ((level / 50) * 4).coerceAtLeast(1)
                    val randomAmount = Random.nextInt(1, maxAmount + 1)
                    p.inventory.add(bonusItemId, randomAmount)
                    p.filterableMessage("You also get $randomAmount x anima-infused bark.")
                }
                p.addXp(Skills.WOODCUTTING, tree.xp)

                if (p.world.random(tree.depleteChance) == 0) {
                    p.animate(-1)

                    if (trunkId != -1) {
                        val world = p.world
                        world.queue {
                            val trunk = DynamicObject(obj, trunkId)
                            world.remove(obj)
                            world.spawn(trunk)
                            wait(tree.respawnTime.random())
                            world.remove(trunk)
                            world.spawn(DynamicObject(obj))
                        }
                    }
                    break
                }
            }
            it.wait(2)
        }
    }

    private fun canChop(p: Player, obj: GameObject, tree: TreeType): Boolean {
        if (!p.world.isSpawned(obj)) {
            return false
        }

        // Kies de beste axe die je kunt gebruiken
        val axe = AxeType.values
            .filter { p.getSkills().getBaseLevel(Skills.WOODCUTTING) >= it.level
                    && (p.equipment.contains(it.item) || p.inventory.contains(it.item)) }
            .maxByOrNull { it.level }
        if (axe == null) {
            p.message("You need an axe to chop down this tree.")
            p.message("You do not have an axe which you have the woodcutting level to use.")
            return false
        }

        if (p.getSkills().getBaseLevel(Skills.WOODCUTTING) < tree.level) {
            p.message("You need a Woodcutting level of ${tree.level} to chop down this tree.")
            return false
        }

        if (p.inventory.isFull) {
            p.message("Your inventory is too full to hold any more logs.")
            return false
        }

        return true
    }

    fun createAxe(player: Player) {
        if (player.getSkills().getBaseLevel(Skills.WOODCUTTING) >= 61 && player.getSkills().getBaseLevel(Skills.FIREMAKING) >= 85) {
            player.inventory.remove(Items.DRAGON_AXE)
            player.inventory.remove(Items.SMOULDERING_STONE)
            player.inventory.add(Items.INFERNAL_AXE)

            player.attr.put(infernalAxe, 5000)

            player.animate(id = 4511, delay = 2)
            player.graphic(id = 1240, height = 2)

            player.addXp(Skills.FIREMAKING, 350.0)
            player.addXp(Skills.WOODCUTTING, 200.0)
        } else if (player.getSkills().getBaseLevel(Skills.FIREMAKING) < 85 || player.getSkills().getBaseLevel(Skills.WOODCUTTING) < 61 &&
            player.getSkills().getBaseLevel(Skills.FIREMAKING) >= 85 || player.getSkills().getBaseLevel(Skills.WOODCUTTING) >= 61) {
            player.message("You need 61 woodcrafing and 85 firemaking to make this")
        }
    }

    fun checkCharges(p: Player) {
        p.message("Your infernal axe currently has ${p.attr.get(infernalAxe)} charges left.")
    }
}
