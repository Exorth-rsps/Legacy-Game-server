package org.alter.plugins.content.area.legacy.entrana.chat

arrayOf(Npcs.MONK_OF_ENTRANA).forEach { npc ->
    on_npc_option(npc = npc, option = "talk-to", lineOfSightDistance = 1) { player.queue { dialog() } }
    //on_npc_option(npc = npc, option = "take-boat", lineOfSightDistance = 1) { teleport_me(player) }
    on_npc_option(npc = npc, option = "take-boat", lineOfSightDistance = 1) { player.queue { teleport_me() } }
}

suspend fun QueueTask.dialog() {
    chatNpc("Can I help you?")
    var continueDialog = true
    while (continueDialog) {
        when (options()) {
            1 -> {
                teleport_me()
                continueDialog = false
            }
            2 -> {
                no_thank_you()
                continueDialog = false
            }
        }
    }
}

suspend fun QueueTask.options(): Int = options(
    "Can you sail me to Mot'ton? (Costs 1K)",
    "No thank you."
)

suspend fun QueueTask.no_thank_you() {
    chatPlayer("No thank you.", animation = 568)
    chatNpc("Well have a good day.", animation = 554)
}

suspend fun QueueTask.teleport_me() {
    chatPlayer("Can you sail me to Mot'ton?", animation = 568)
    //teleport(player)
    chatNpc("No.... i cant at this moment!", animation = 554)
    chatPlayer("Why not?", animation = 568)
    chatNpc("I must first repair the ship!", animation = 554)
    chatPlayer("....", animation = 568)
}

//fun teleport(p: Player) {
//    p.queue {
//        val npc = player.getInteractingNpc()
//        player.lock = LockState.FULL
//        npc.forceChat("Senventior Disthine Molenko")
//        npc.graphic(108, 10)
//        wait(3)
//        player.graphic(109, 125) // This remains a projectile
//        wait(2)
//        player.graphic(110, 125)
//        wait(2)
//        player.moveTo(3616, 1245, 0)
//        player.lock = LockState.NONE
//    }
//}
