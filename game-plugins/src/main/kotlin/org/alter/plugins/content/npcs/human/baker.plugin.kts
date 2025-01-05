package org.alter.plugins.content.npcs.human;

val ids = intArrayOf(
    Npcs.BAKER_8724,
    Npcs.BAKER_8725
)


val BAKER_YELL_DELAY = TimerKey()

val bakerShouts = listOf(
    "Buy your best cakes in town!",
    "Freshly baked bread and cakes!",
    "Come and get the tastiest treats!",
    "Hot cakes, straight from the oven!",
    "Don’t miss out on our delicious pastries!"
)

ids.forEach { baker ->
    on_npc_spawn(npc = baker) {
        val npc = npc
        npc.timers[BAKER_YELL_DELAY] = world.random(10..100)
    }
}

on_timer(BAKER_YELL_DELAY) {
    val npc = npc
    val randomShout = bakerShouts.random() // Pick a random shout from the list
    npc.forceChat(randomShout)
    npc.timers[BAKER_YELL_DELAY] = world.random(10..100)
}
