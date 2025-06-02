package org.alter.plugins.content.area.legacy.entrana.chat

arrayOf(Npcs.MONK_2579).forEach { npc ->
    on_npc_option(npc = npc, option = "talk-to", lineOfSightDistance = 1) { player.queue { dialog() } }
}

suspend fun QueueTask.dialog() {
    chatNpc("Greetings traveller.")
    var continueDialog = true
    while (continueDialog) {
        when (options(
            "Can you heal me? I'm injured.",
            "Isn't this place built a bit out of the way?"
        )) {
            1 -> {
                heal_request()
                continueDialog = false
            }
            2 -> {
                ask_about_location()
                continueDialog = false
            }
        }
    }
}

suspend fun QueueTask.heal_request() {
    chatPlayer("Can you heal me? I'm injured.", animation = 568)
    chatNpc("Ok.", animation = 554)
    healing(player)
}

suspend fun QueueTask.ask_about_location() {
    chatPlayer("Isn't this place built a bit out of the way?", animation = 568)
    chatNpc("We like it that way actually! We get disturbed less.", animation = 554)
    chatNpc("We still get rather a large amount of travellers looking for sanctuary and healing here as it is!", animation = 554)
}

fun healing(p: Player) {
    p.queue {
        val npc = player.getInteractingNpc()
        player.lock = LockState.FULL
        npc.forceChat("By the power of Saradomin!")
        npc.graphic(108, 10)
        player.heal(player.getSkills().getMaxLevel(Skills.HITPOINTS))
        player.message("You feel completely healed.")
        player.lock = LockState.NONE
    }
}
