package org.alter.plugins.content.skills.herblore.pots

Pots.values().forEach { p ->
    p.pot.secondaries.forEach { secondary ->
        on_item_on_item(secondary, p.pot.unfinished) {
            val max = player.maxPossible(secondary, p.pot.unfinished)
            player.produceItemBoxMessage(p.pot.finished[2], max = max, growingDelay = true) {
                player.queue {
                    p.pot.make(player)
                }
            }
        }
    }
}



