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
    // Voorbeeld: een chest die Cold Key nodig heeft
    MUDDYCHEST(intArrayOf(Objs.CLOSED_CHEST_375), Items.MUDDY_KEY, 300,
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
            player.filterableMessage("You attempt to unlock $attemptMsg from the ${chestId.getObjName(player.world.definitions, lowercase = true)}.")
        }
        // 4) pick loot
        val item = getStolenItem(steals)
        if (player.inventory.add(item).hasSucceeded()) {
            player.queue {
                player.lock()
                wait(1)
                player.animate(832)
                player.playSound(2581)
                player.inventory.remove(Items.MUDDY_KEY, 1)
                val outMsg = "You retrieve ${Misc.getIndefiniteArticle(item.getName(player.world.definitions).lowercase())}."
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

    private fun getStolenItem(steals: Array<ChestItem>): Item {
        val candidates = steals.filter { Math.random() * 100 <= it.chance }
            .map { Item(it.itemId, it.amount) }
        return candidates.randomOrNull() ?: Item(steals.first().itemId, steals.first().amount)
    }

    private fun getEmptyChestId(chestId: Int): Int = when (chestId) {
        Objs.CHEST_2587 -> Objs.CHEST_2588
        else                -> chestId
    }
}

// In je plugin: hook het Open‑option
on_obj_option(obj = Objs.CHEST_2587, option = "Open", lineOfSightDistance = 1) {
    KeyChest.MUDDYCHEST.attempt(player, player.getInteractingGameObj().id)
}

object ChestRewards {

    val muddy_chest = arrayOf(
        ChestItem(Items.COINS_995, amount = 2000, chance = 65.0),
        ChestItem(Items.COINS_995, amount = 3000, chance = 25.0),
        ChestItem(Items.COINS_995, amount = 4000, chance = 10.0),
        ChestItem(Items.HIGHWAYMAN_MASK, amount = 1, chance = 0.002),
        ChestItem(Items.WIZARD_BOOTS, amount = 1, chance = 0.001),
        ChestItem(Items.RANGER_BOOTS, amount = 1, chance = 0.001),
    )

}