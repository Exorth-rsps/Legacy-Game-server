package org.alter.plugins.content.area.legacy.barrows

import org.alter.api.cfg.Objs
import org.alter.game.model.Tile
import org.alter.game.model.entity.Npc

private val SARCOPHAGI = intArrayOf(Objs.SARCOPHAGUS_20720, Objs.SARCOPHAGUS_20721, Objs.SARCOPHAGUS_20722)

SARCOPHAGI.forEach { sarc ->
    on_obj_option(obj = sarc, option = "search") {
        val index = Barrows.BROTHERS.indexOfFirst { player.tile.isWithinRadius(it.crypt, 2) }
        if (index == -1) {
            player.message("You find nothing of interest.")
            return@on_obj_option
        }
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
            val npc = Npc(player, Barrows.BROTHERS[index].id, player.tile.transform(1, 0), world)
            npc.respawns = false
            world.spawn(npc)
        }
    }
}
