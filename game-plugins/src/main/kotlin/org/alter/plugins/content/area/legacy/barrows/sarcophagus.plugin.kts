package org.alter.plugins.content.area.legacy.barrows

import org.alter.game.model.Tile
import org.alter.game.model.entity.Npc
import org.alter.plugins.content.area.legacy.barrows.Barrows
import org.alter.plugins.content.magic.TeleportType
import org.alter.plugins.content.magic.teleport


private val SARCOPHAGI = Barrows.SARCOPHAGUS_IDS

SARCOPHAGI.forEachIndexed { index, sarc ->
    listOf("Search").forEach { opt ->
        on_obj_option(obj = sarc, option = opt) {
            if (index == Barrows.TUNNEL_INDEX) {
                player.moveTo(Tile(3551, 9691))
                return@on_obj_option
            }
            val progress = player.attr[Barrows.PROGRESS_ATTR] ?: 0
            if (progress and (1 shl index) != 0) {
                player.message("The sarcophagus is empty.")
                return@on_obj_option
            }
            val spawned = world.npcs.any { it.owner == player && it.id == Barrows.BROTHERS[index].id }
            if (!spawned) {
                val npc = Npc(player, Barrows.BROTHERS[index].id, player.tile.transform(1, 1), world)
                npc.respawns = false
                world.spawn(npc)
            }

        }
    }
}
on_obj_option(Objs.STAIRCASE_20672, option = "Climb-up") {
    player.moveTo(Tile(3556, 3298, 0))
}
on_obj_option(Objs.STAIRCASE_20668, option = "Climb-up") {
    player.moveTo(Tile(3575, 3298, 0))
}
on_obj_option(Objs.STAIRCASE_20667, option = "Climb-up") {
    player.moveTo(Tile(3565, 3290, 0))
}
on_obj_option(Objs.STAIRCASE_20671, option = "Climb-up") {
    player.moveTo(Tile(3553, 3283, 0))
}
on_obj_option(Objs.STAIRCASE_20670, option = "Climb-up") {
    player.moveTo(Tile(3565, 3276, 0))
}
on_obj_option(Objs.STAIRCASE_20669, option = "Climb-up") {
    player.moveTo(Tile(3578, 3282, 0))
}