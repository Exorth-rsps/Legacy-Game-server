package org.alter.plugins.content.skills.mining

import org.alter.game.fs.def.ItemDef
import org.alter.game.fs.def.ObjectDef
import org.alter.game.model.attr.AttributeKey
import org.alter.game.model.entity.DynamicObject
import org.alter.game.model.entity.GameObject
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.api.EquipmentType
import org.alter.api.Skills
import org.alter.api.cfg.Items
import org.alter.api.cfg.Sound
import org.alter.api.ext.*
import kotlin.math.min
import kotlin.random.Random

object Mining {

    private const val MINING_ANIMATION_TIME = 16

    /** Per-object cooldown om spam-klikken te blokkeren */
    private val LAST_CLICK = AttributeKey<Long>("lastMineClick")

    suspend fun mineRock(it: QueueTask, obj: GameObject, rock: RockType) {
        val player = it.player

        // voorkom direct herhaald klikken binnen 600ms
        val now = System.currentTimeMillis()
        val last = obj.attr[LAST_CLICK] ?: 0L
        if (now - last < 600) {
            // je kunt even niet opnieuw klikken
            return
        }
        obj.attr[LAST_CLICK] = now

        if (!canMine(it, player, obj, rock)) {
            return
        }

        val oreName = player.world.definitions
            .get(ItemDef::class.java, rock.reward)
            .name
            .lowercase()

        player.filterableMessage("You swing your pick at the rock.")

        var animations = 0
        var ticks = 0
        val pick = PickaxeType.values.reversed().first {
            player.getSkills().getMaxLevel(Skills.MINING) >= it.level &&
                    (player.equipment.contains(it.item) || player.inventory.contains(it.item))
        }

        while (canMine(it, player, obj, rock)) {
            val animationWait = if (animations < 2)
                MINING_ANIMATION_TIME + 1
            else
                MINING_ANIMATION_TIME

            if (ticks % animationWait == 0) {
                player.animate(pick.animation, delay = 30)
                animations++
            }

            if (ticks % pick.ticksBetweenRolls == 0) {
                val level = player.getSkills().getCurrentLevel(Skills.MINING)
                val baseChance = interpolate(rock.lowChance, rock.highChance, level)

                if (pick == PickaxeType.DRAGON) {
                    player.miningAccumulator += 0.2f
                }

                if (baseChance > Random.nextInt(255)) {
                    onSuccess(player, oreName, rock, obj)
                }
            }

            if (pick == PickaxeType.DRAGON && player.miningAccumulator >= 1f) {
                val level = player.getSkills().getCurrentLevel(Skills.MINING)
                val bonusChance = interpolate(rock.lowChance, rock.highChance, level) * 1.12
                if (bonusChance > Random.nextInt(255)) {
                    onSuccess(player, oreName, rock, obj)
                }
                player.miningAccumulator -= 1f
            }

            val waitTime = min(
                animationWait - (ticks % animationWait),
                pick.ticksBetweenRolls - (ticks % pick.ticksBetweenRolls)
            )
            it.wait(waitTime)
            ticks += waitTime
        }

        player.animate(-1)
    }

    private fun onSuccess(player: Player, oreName: String, rock: RockType, obj: GameObject) {
        val world = player.world

        val chanceOfGem = if (player.hasEquipped(
                EquipmentType.AMULET,
                Items.AMULET_OF_GLORY, Items.AMULET_OF_GLORY1, Items.AMULET_OF_GLORY2,
                Items.AMULET_OF_GLORY3, Items.AMULET_OF_GLORY4, Items.AMULET_OF_GLORY5,
                Items.AMULET_OF_GLORY6, Items.AMULET_OF_GLORY_T, Items.AMULET_OF_GLORY_T1,
                Items.AMULET_OF_GLORY_T2, Items.AMULET_OF_GLORY_T3, Items.AMULET_OF_GLORY_T4,
                Items.AMULET_OF_GLORY_T5, Items.AMULET_OF_GLORY_T6, Items.AMULET_OF_GLORY_8283,
                Items.AMULET_OF_GLORY_20586
            )
        ) {
            world.random(86)
        } else {
            world.random(256)
        }

        if (chanceOfGem == 1 && rock != RockType.ESSENCE) {
            player.inventory.add(
                Items.UNCUT_DIAMOND + (world.random(0..3) * 2)
            )
        }
        if (rock.isGemRock) {
            rock.drop(player)
            return
        }

        if (player.hasEquipped(
                EquipmentType.CHEST,
                Items.VARROCK_ARMOUR_1, Items.VARROCK_ARMOUR_2,
                Items.VARROCK_ARMOUR_3, Items.VARROCK_ARMOUR_4
            ) && rock != RockType.ESSENCE
        ) {
            if ((rock.varrockArmourAffected -
                        (player.getEquipment(EquipmentType.CHEST)?.id ?: -1)) >= 0) {
                player.inventory.add(rock.reward)
            }
        }

        val reward = if (rock == RockType.ESSENCE &&
            player.getSkills().getCurrentLevel(Skills.MINING) >= 30
        ) Items.PURE_ESSENCE else rock.reward

        val def = world.definitions.get(ObjectDef::class.java, obj.id)
        if (def.depleted != -1) {
            world.queue {
                val depleted = DynamicObject(obj, def.depleted)
                world.remove(obj)
                world.spawn(depleted)
                wait(rock.respawnDelay)
                world.remove(depleted)
                world.spawn(DynamicObject(obj))
            }
            player.playSound(Sound.MINE_ORE)
        }

        player.inventory.add(reward)
        player.addXp(Skills.MINING, rock.experience)

        if (Random.nextInt(5) == 0) {
            val level = player.getSkills().getCurrentLevel(Skills.MINING)
            val maxAmt = ((level / 50) * 4).coerceAtLeast(1)
            val amt = Random.nextInt(1, maxAmt + 1)
            player.inventory.add(Items.UNIDENTIFIED_MINERALS, amt)
            player.filterableMessage("You also get $amt unidentified minerals.")
        }

        player.filterableMessage("You manage to mine some $oreName.")
    }

    private suspend fun canMine(
        it: QueueTask, p: Player, obj: GameObject, rock: RockType
    ): Boolean {
        if (!p.world.isSpawned(obj)) return false

        val pick = PickaxeType.values.reversed().firstOrNull {
            p.getSkills().getMaxLevel(Skills.MINING) >= it.level &&
                    (p.equipment.contains(it.item) || p.inventory.contains(it.item))
        }
        if (pick == null) {
            it.messageBox(
                "You need a pickaxe to mine this rock. You do not have a " +
                        "pickaxe which you have the Mining level to use."
            )
            return false
        }
        if (p.getSkills().getBaseLevel(Skills.MINING) < rock.level) {
            it.messageBox("You need a Mining level of ${rock.level} to mine this rock.")
            return false
        }
        if (p.inventory.isFull) {
            it.messageBox(
                "Your inventory is too full to hold any more " +
                        if (rock == RockType.ESSENCE) "essence" else "ores"
            )
            return false
        }
        return true
    }
}
