package org.alter.plugins.content.npcs.human

val gemMerchantIds = intArrayOf(
    Npcs.GEM_MERCHANT_4581
)

val GEM_MERCHANT_YELL_DELAY = TimerKey()

val gemMerchantShouts = listOf(
    "Shiny gems for sale, perfect for crafting!",
    "Enhance your jewelry with these rare gems!",
    "Finest emeralds, rubies, and diamonds right here!",
    "Don’t miss out on these sparkling deals!",
    "Gems to dazzle your friends and foes alike!"
)

gemMerchantIds.forEach { gemMerchant ->
    on_npc_spawn(npc = gemMerchant) {
        val npc = npc
        npc.timers[GEM_MERCHANT_YELL_DELAY] = world.random(10..100)
    }
}

on_timer(GEM_MERCHANT_YELL_DELAY) {
    val npc = npc
    val randomShout = gemMerchantShouts.random() // Pick a random shout from the list
    npc.forceChat(randomShout)
    npc.timers[GEM_MERCHANT_YELL_DELAY] = world.random(10..100)
}
