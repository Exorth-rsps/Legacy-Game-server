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
    "Can you sail me to Mot'ton?",
    "No thank you."
)

suspend fun QueueTask.no_thank_you() {
    chatPlayer("No thank you.", animation = 568)
    chatNpc("Well have a good day.", animation = 554)
}

suspend fun QueueTask.teleport_me() {
    chatPlayer("Can you sail me to Mot'ton?", animation = 568)
    chatNpc("Have a good sail!", animation = 554)
    teleport(player)
}

fun teleport(p: Player) {
    p.queue {
        val npc = player.getInteractingNpc()
        player.lock = LockState.FULL
        player.moveTo(3521, 3285, 0)
        player.lock = LockState.NONE
    }
}
