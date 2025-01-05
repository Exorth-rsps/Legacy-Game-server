package org.alter.plugins.content.npcs.human

val silverMerchantIds = intArrayOf(
    Npcs.SILVER_MERCHANT
)

val SILVER_MERCHANT_YELL_DELAY = TimerKey()

val silverMerchantShouts = listOf(
    "Fine silver goods for sale, crafted to perfection!",
    "Enhance your look with premium silver accessories!",
    "Silverware, jewelry, and more, all right here!",
    "Shiny and affordable silver items for everyone!",
    "Don’t miss these dazzling silver deals!"
)

silverMerchantIds.forEach { silverMerchant ->
    on_npc_spawn(npc = silverMerchant) {
        val npc = npc
        npc.timers[SILVER_MERCHANT_YELL_DELAY] = world.random(10..100)
    }
}

on_timer(SILVER_MERCHANT_YELL_DELAY) {
    val npc = npc
    val randomShout = silverMerchantShouts.random() // Pick a random shout from the list
    npc.forceChat(randomShout)
    npc.timers[SILVER_MERCHANT_YELL_DELAY] = world.random(10..100)
}
