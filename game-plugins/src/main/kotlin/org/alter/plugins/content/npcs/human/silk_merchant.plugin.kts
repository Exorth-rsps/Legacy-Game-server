package org.alter.plugins.content.npcs.human

val silkMerchantIds = intArrayOf(
    Npcs.SILK_MERCHANT_4583
)

val SILK_MERCHANT_YELL_DELAY = TimerKey()

val silkMerchantShouts = listOf(
    "Finest silks from across the world, don’t miss out!",
    "Luxurious silk for the best clothing and decor!",
    "Get your soft and elegant silks here!",
    "High-quality silk at unbeatable prices!",
    "Silk so fine, you’ll feel like royalty!"
)

silkMerchantIds.forEach { silkMerchant ->
    on_npc_spawn(npc = silkMerchant) {
        val npc = npc
        npc.timers[SILK_MERCHANT_YELL_DELAY] = world.random(10..100)
    }
}

on_timer(SILK_MERCHANT_YELL_DELAY) {
    val npc = npc
    val randomShout = silkMerchantShouts.random() // Pick a random shout from the list
    npc.forceChat(randomShout)
    npc.timers[SILK_MERCHANT_YELL_DELAY] = world.random(10..100)
}
