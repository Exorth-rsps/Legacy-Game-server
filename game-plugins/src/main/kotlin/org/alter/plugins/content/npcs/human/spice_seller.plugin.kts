package org.alter.plugins.content.npcs.human

val spiceSellerIds = intArrayOf(
    Npcs.SPICE_SELLER_4579
)

val SPICE_SELLER_YELL_DELAY = TimerKey()

val spiceSellerShouts = listOf(
    "Fresh spices and herbs for all your needs!",
    "Get your Herblore supplies here!",
    "Premium spices straight from the desert!",
    "Boost your potions with the finest herbs!",
    "Herblore enthusiasts, don't miss out on these deals!"
)

spiceSellerIds.forEach { spiceSeller ->
    on_npc_spawn(npc = spiceSeller) {
        val npc = npc
        npc.timers[SPICE_SELLER_YELL_DELAY] = world.random(10..100)
    }
}

on_timer(SPICE_SELLER_YELL_DELAY) {
    val npc = npc
    val randomShout = spiceSellerShouts.random() // Pick a random shout from the list
    npc.forceChat(randomShout)
    npc.timers[SPICE_SELLER_YELL_DELAY] = world.random(10..100)
}
