package org.alter.plugins.content.area.legacy.dwarven_mine.obs

import org.alter.game.model.entity.DynamicObject
import org.alter.game.model.entity.Player
import org.alter.game.model.item.Item
import org.alter.api.cfg.Items
import org.alter.api.cfg.Objs
import org.alter.api.ext.getObjName
import gg.rsmod.util.Misc
import org.alter.plugins.content.skills.thieving.objs.ChestItem

enum class KeyChest(
    val chestIds: IntArray,     // de obj IDs van de dichte kist
    private val keyItem: Int,   // de benodigde sleutel
    private val respawnCycles: Int,
    val attemptMsg: String = "",
    val steals: Array<ChestItem>
) {
    MUDDYCHEST(
        intArrayOf(Objs.CLOSED_CHEST_375),
        Items.MUDDY_KEY,
        respawnCycles = 30,
        attemptMsg = "Lock on the muddy chest",
        steals = ChestRewards.muddy_chest
    );

    fun attempt(player: Player, chestId: Int) {
        // 1) ruimte?
        if (!player.inventory.hasSpace) {
            player.queue { messageBox("Your inventory is too full to hold any more.") }
            return
        }
        // 2) key?
        if (!player.inventory.contains(keyItem)) {
            player.queue {
                val keyName = Item(keyItem).getName(player.world.definitions).lowercase()
                messageBox("You need a $keyName to open this chest.")
            }
            return
        }
        // 3) feedback
        if (attemptMsg.isNotEmpty()) {
            player.filterableMessage(
                "You attempt to unlock $attemptMsg from the ${chestId.getObjName(player.world.definitions, lowercase = true)}."
            )
        }

        // --- selecteer eerst het ChestItem zodat we de kans kennen ---
        val stolenChestItem = selectChestItem(steals)
        val item = Item(stolenChestItem.itemId, stolenChestItem.amount)

        // 4) loot toevoegen
        if (player.inventory.add(item).hasSucceeded()) {
            player.queue {
                player.lock()
                wait(1)
                player.animate(832)
                player.playSound(2581)
                player.inventory.remove(keyItem, 1)

                // berichten naar speler
                val outMsg = "You retrieve ${Misc.getIndefiniteArticle(item.getName(player.world.definitions).lowercase())}."

                // 4a) globale boodschap voor zeldzame drops (<1% kans)
                if (stolenChestItem.chance < 1.0) {
                    val playerName = player.username
                    val itemName = item.getName(player.world.definitions).lowercase()
                    val chestName = chestId.getObjName(player.world.definitions, lowercase = true)
                    player.world.players.forEach { p ->
                        p.message(
                            "<col=ff0000>[GLOBAL]</col> $playerName has obtained a $itemName from the Muddychest!",
                            ChatMessageType.CONSOLE
                        )
                    }
                }

                // 5) respawn chest
                player.world.queue {
                    val obj = player.getInteractingGameObj()
                    player.world.remove(obj)
                    player.message(outMsg)
                    val empty = DynamicObject(obj, getEmptyChestId(chestId))
                    player.world.spawn(empty)
                    wait(respawnCycles)
                    player.world.remove(empty)
                    player.world.spawn(DynamicObject(obj))
                }
                player.unlock()
            }
        }
    }

    /** Helper die de ChestItem selecteert op basis van kans */
    private fun selectChestItem(steals: Array<ChestItem>): ChestItem {
        val candidates = steals.filter { Math.random() * 100 <= it.chance }
        return candidates.randomOrNull() ?: steals.first()
    }

    private fun getEmptyChestId(chestId: Int): Int = when (chestId) {
        Objs.CHEST_2587 -> Objs.CHEST_2588
        else            -> chestId
    }
}

object ChestRewards {
    val muddy_chest = arrayOf(
        ChestItem(Items.STEEL_BAR_NOTED, amount = 15, chance = 65.0),
        ChestItem(Items.MITHRIL_BAR_NOTED, amount = 10, chance = 25.0),
        ChestItem(Items.COINS_995,        amount = 2000, chance = 10.0),
        ChestItem(Items.RUNE_PICKAXE,     amount = 1,    chance = 0.002),
        ChestItem(Items.DRAGON_PICKAXE,   amount = 1,    chance = 0.001),
        ChestItem(Items.RUNITE_BAR_NOTED, amount = 15,   chance = 0.001)
    )
}

// Hook the Open–option op de chest
on_obj_option(obj = Objs.CHEST_2587, option = "Open", lineOfSightDistance = 1) {
    KeyChest.MUDDYCHEST.attempt(player, player.getInteractingGameObj().id)
}
