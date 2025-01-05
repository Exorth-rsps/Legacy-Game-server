package org.alter.plugins.content.npcs.human

val furTraderIds = intArrayOf(
    Npcs.FUR_TRADER_4580
)

val FUR_TRADER_YELL_DELAY = TimerKey()

val furTraderShouts = listOf(
    "Fine furs for sale, perfect for keeping warm!",
    "Exotic furs from across the lands, don’t miss out!",
    "Get your luxurious furs right here!",
    "High-quality furs, ideal for crafting and style!",
    "Don’t let the cold get to you—grab a fur coat today!"
)

furTraderIds.forEach { furTrader ->
    on_npc_spawn(npc = furTrader) {
        val npc = npc
        npc.timers[FUR_TRADER_YELL_DELAY] = world.random(10..100)
    }
}

on_timer(FUR_TRADER_YELL_DELAY) {
    val npc = npc
    val randomShout = furTraderShouts.random() // Pick a random shout from the list
    npc.forceChat(randomShout)
    npc.timers[FUR_TRADER_YELL_DELAY] = world.random(10..100)
}
