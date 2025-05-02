package org.alter.plugins.content.skills.mining

import org.alter.game.fs.def.ItemDef
import org.alter.game.fs.def.ObjectDef
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

// Attribute key to prevent essence-rock spam-clicking
private val IS_MINING_ESSENCE = Attr.key<Boolean>("isMiningEssence")

object Mining {

    private const val MINING_ANIMATION_TIME = 16

    suspend fun mineRock(it: QueueTask, obj: GameObject, rock: RockType) {
        val player = it.player

        // === Essence-spam-prevention ===
        if (rock == RockType.ESSENCE) {
            if (player.attr[IS_MINING_ESSENCE] == true) {
                player.filterableMessage("You are already mining that essence rock, please wait…")
                return
            }
            player.attr[IS_MINING_ESSENCE] = true
        }

        try {
            if (!canMine(it, player, obj, rock)) {
                return
            }
            val oreName = player.world.definitions.get(ItemDef::class.java, rock.reward)
                .name.lowercase()
            var animations = 0
            val pick = PickaxeType.values.reversed().firstOrNull {
                player.getSkills().getMaxLevel(Skills.MINING) >= it.level &&
                        (player.equipment.contains(it.item) || player.inventory.contains(it.item))
            }!!

            player.filterableMessage("You swing your pick at the rock.")
            var ticks = 0

            while (canMine(it, player, obj, rock)) {
                val animationWait = if (animations < 2) MINING_ANIMATION_TIME + 1 else MINING_ANIMATION_TIME
                if (ticks % animationWait == 0) {
                    player.animate(pick.animation, delay = 30)
                    animations++
                }

                if (ticks % pick.ticksBetweenRolls == 0) {
                    val level = player.getSkills().getCurrentLevel(Skills.MINING)
                    val baseChance = interpolate(rock.lowChance, rock.highChance, level)

                    if (pick == PickaxeType.DRAGON) {
                        player.miningAccumulator += 0.2
                    }

                    if (baseChance > RANDOM.nextInt(255)) {
                        onSuccess(player, oreName, rock, obj)
                    }
                }

                if (player.miningAccumulator >= 1 && pick == PickaxeType.DRAGON) {
                    val level = player.getSkills().getCurrentLevel(Skills.MINING)
                    val baseChance = interpolate(rock.lowChance, rock.highChance, level) * 1.12

                    if (baseChance > RANDOM.nextInt(255)) {
                        onSuccess(player, oreName, rock, obj)
                    }

                    player.miningAccumulator -= 1
                }

                val time = min(
                    animationWait - ticks % animationWait,
                    pick.ticksBetweenRolls - ticks % pick.ticksBetweenRolls
                )
                it.wait(time)
                ticks += time
            }

            player.animate(-1)
        } finally {
            // Reset the essence-spam flag when mining finishes or is interrupted
            if (rock == RockType.ESSENCE) {
                player.removeAttr(IS_MINING_ESSENCE)
            }
        }
    }

    private fun onSuccess(player: Player, oreName: String, rock: RockType, obj: GameObject) {
        val world = player.world
        val chanceOfGem = if (player.hasEquipped(
                EquipmentType.AMULET,
                Items.AMULET_OF_GLORY,
                Items.AMULET_OF_GLORY1,
                Items.AMULET_OF_GLORY2,
                Items.AMULET_OF_GLORY3,
                Items.AMULET_OF_GLORY4,
                Items.AMULET_OF_GLORY5,
                Items.AMULET_OF_GLORY6,
                Items.AMULET_OF_GLORY_T,
                Items.AMULET_OF_GLORY_T1,
                Items.AMULET_OF_GLORY_T2,
                Items.AMULET_OF_GLORY_T3,
                Items.AMULET_OF_GLORY_T4,
                Items.AMULET_OF_GLORY_T5,
                Items.AMULET_OF_GLORY_T6,
                Items.AMULET_OF_GLORY_8283,
                Items.AMULET_OF_GLORY_20586
            )
        ) {
            player.world.random(86)
        } else {
            player.world.random(256)
        }

        if (chanceOfGem == 1 && rock != RockType.ESSENCE) {
            player.inventory.add(Items.UNCUT_DIAMOND + (player.world.random(0..3) * 2))
        }

        if (rock.isGemRock) {
            rock.drop(player)
            return
        }

        if (player.hasEquipped(
                EquipmentType.CHEST,
                Items.VARROCK_ARMOUR_1,
                Items.VARROCK_ARMOUR_2,
                Items.VARROCK_ARMOUR_3,
                Items.VARROCK_ARMOUR_4
            ) && rock != RockType.ESSENCE
        ) {
            if ((rock.varrockArmourAffected - (player.getEquipment(EquipmentType.CHEST)?.id ?: -1)) >= 0) {
                player.inventory.add(rock.reward)
            }
        }

        val reward = if (rock == RockType.ESSENCE && player.skillSet
                .getCurrentLevel(Skills.MINING) >= 30
        ) Items.PURE_ESSENCE else rock.reward

        val depletedRockId = player.world.definitions.get(ObjectDef::class.java, obj.id).depleted
        if (depletedRockId != -1) {
            world.queue {
                val depletedOre = DynamicObject(obj, depletedRockId)
                world.remove(obj)
                world.spawn(depletedOre)
                wait(rock.respawnDelay)
                world.remove(depletedOre)
                world.spawn(DynamicObject(obj))
            }
            player.playSound(Sound.MINE_ORE)
        }

        player.inventory.add(reward)
        player.addXp(Skills.MINING, rock.experience)

        val level = player.getSkills().getCurrentLevel(Skills.MINING)
        if (Random.nextInt(5) == 0) {
            val bonusItemId = Items.UNIDENTIFIED_MINERALS
            val maxAmount = ((level / 10) * 5).coerceAtLeast(1)
            val randomAmount = Random.nextInt(1, maxAmount + 1)
            player.inventory.add(bonusItemId, randomAmount)
            player.filterableMessage("You also get $randomAmount unidentified minerals.")
        }

        player.filterableMessage("You manage to mine some $oreName.")
    }

    private suspend fun canMine(it: QueueTask, p: Player, obj: GameObject, rock: RockType): Boolean {
        if (!p.world.isSpawned(obj)) {
            return false
        }
        val pick = PickaxeType.values.reversed().firstOrNull {
            p.getSkills().getMaxLevel(Skills.MINING) >= it.level &&
                    (p.equipment.contains(it.item) || p.inventory.contains(it.item))
        }
        if (pick == null) {
            it.messageBox(
                "You need a pickaxe to mine this rock. " +
                        "You do not have a pickaxe that you have the Mining level to use."
            )
            return false
        }
        if (p.getSkills().getBaseLevel(Skills.MINING) < rock.level) {
            it.messageBox("You need a Mining level of ${rock.level} to mine this rock.")
            return false
        }
        if (p.inventory.isFull) {
            val what = if (rock == RockType.ESSENCE) "essence" else "ores"
            it.messageBox("Your inventory is too full to hold any more $what.")
            return false
        }
        return true
    }
}
