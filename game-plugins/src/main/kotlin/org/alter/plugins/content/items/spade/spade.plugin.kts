package org.alter.plugins.content.items.spade

import org.alter.api.cfg.Items
import org.alter.game.model.entity.Npc
import org.alter.plugins.content.area.legacy.barrows.Barrows

on_item_option(item = Items.SPADE, "dig") {
    player.animate(830)
    val loc = player.tile

    // 1) Barrows-logica: 1 loop, in plaats van twee
    Barrows.BROTHERS.forEach { brother ->
        if (loc.isWithinRadius(brother.mound, 1)) {
            // Kijk eerst of er al een NPC bij de crypte staat:
            val alreadySpawned = world.npcs.any { it.id == brother.id && it.tile == brother.crypt }
            if (!alreadySpawned) {
                // Spawn de Barrows-broeder als hij er nog niet is
                val npc = Npc(brother.id, brother.crypt, world)
                world.spawn(npc)
            }
            // Verplaats de speler naar de crypte, ongeacht of we net gespawnd hebben
            player.moveTo(brother.crypt)
            return@on_item_option
        }
    }
    if (player.tile.x == 3229 && player.tile.z == 3209 && player.inventory.contains(Items.TREASURE_SCROLL)) {
        player.queue {
            player.inventory.remove(23067, 1)
            player.inventory.add(23068, 1)
            player.setVarp(2111, 2)
            itemMessageBox("You dig up a Treasure Scroll.", item = 23068)
        }
    } else if (player.tile.x == 3202 && player.tile.z == 3211 && player.inventory.contains(Items.TREASURE_SCROLL_23068)) {
        player.queue {
            player.inventory.remove(23068, 1)
            player.inventory.add(23069, 1)
            player.setVarp(2111, 3)
            itemMessageBox("You dig up a Mysterious Orb.", item = 23069)
        }
    } else if (player.tile.x == 3108 && player.tile.z == 3264 && player.inventory.contains(Items.MYSTERIOUS_ORB_23069)) {
        player.queue {
            player.inventory.remove(23069, 1)
            player.inventory.add(23070, 1)
            player.setVarp(2111, 4)
            itemMessageBox("You dig up a Treasure Scroll.", item = 23070)
        }
    } else if (player.tile.x == 3077 && player.tile.z == 3260 && player.inventory.contains(Items.TREASURE_SCROLL_23070)) {
        player.queue {
            player.inventory.remove(23070, 1)
            player.inventory.add(23071, 1)
            player.setVarp(2111, 5)
            itemMessageBox(
                "You dig up an Ancient Casket. As you do, you hear a<br>faint whispering. You can't make out what it says<br>though...",
                item = 23071
            )
            chatPlayer("Hmmmm... Must have been the wind.")
            chatPlayer("Anyway, this must be the treasure that Veos is after. I<br>should take it to him. If I remember right, he's docked<br>at the northernmost pier in Port Sarim.")
        }
    } else player.message("Nothing interesting happens.")
}