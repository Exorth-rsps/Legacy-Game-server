package org.alter.plugins.content.area.legacy.falador.chat

arrayOf(Npcs.ARCHMAGE_SEDRIDOR_11433).forEach { npc ->
    on_npc_option(npc = npc, option = "talk-to", lineOfSightDistance = 1) { player.queue { dialog() } }
    on_npc_option(npc = npc, option = "teleport", lineOfSightDistance = 1) { teleport(player) }
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
    "Can you teleport me to the Rune Essence Mine?",
    "No thank you."
)

suspend fun QueueTask.no_thank_you() {
    chatPlayer("No thank you.", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
}

suspend fun QueueTask.teleport_me() {
    chatPlayer("Can you teleport me to the Rune Essence Mine?", animation = 568)
    teleport(player)
}

fun teleport(p: Player) {
    p.queue {
        val npc = player.getInteractingNpc()
        player.lock = LockState.FULL
        npc.forceChat("Senventior Disthine Molenko")
        npc.graphic(108, 10)
        wait(3)
        player.graphic(109, 125) // This remains a projectile
        wait(2)
        player.graphic(110, 125)
        wait(2)
        player.moveTo(3616, 1245, 0)
        player.lock = LockState.NONE
    }
}
