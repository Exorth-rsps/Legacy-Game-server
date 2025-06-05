package org.alter.plugins.content.area.legacy.barrows


import org.alter.game.model.entity.Player
import org.alter.plugins.content.area.legacy.barrows.Barrows

Barrows.BROTHERS.forEachIndexed { index, brother ->

    on_npc_death(brother.id) {
        val killer = npc.damageMap.getMostDamage() as? Player ?: return@on_npc_death
        val flags = killer.attr[Barrows.PROGRESS_ATTR] ?: 0
        killer.attr[Barrows.PROGRESS_ATTR] = flags or (1 shl index)
        killer.attr[Barrows.LAST_BROTHER_ATTR] = index
    }
}
