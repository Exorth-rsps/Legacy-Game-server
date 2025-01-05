package org.alter.plugins.content.area.yanille.chat

arrayOf(Npcs.ZAVISTIC_RARVE).forEach { npc ->
    on_npc_option(npc = npc, option = "talk-to", lineOfSightDistance = 1) { player.queue { dialog() } }
}

suspend fun QueueTask.dialog() {
    chatNpc("Ah, greetings! Do you need something?")
    var continueDialog = true
    while (continueDialog) {
        when (options()) {
            1 -> {
                ask_about_portals()
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
    "What are you working on?",
    "No thank you."
)

suspend fun QueueTask.no_thank_you() {
    chatPlayer("No thank you.", animation = 568)
    chatNpc("Very well, let me get back to my work.", animation = 554)
}

suspend fun QueueTask.ask_about_portals() {
    chatPlayer("What are you working on?", animation = 568)
    chatNpc("I'm trying to activate these three portals.", animation = 554)
    chatNpc("The eastern portal should connect to the Wizard's Tower, but I'm getting no response from the other side.", animation = 554)
    chatPlayer("What about the southern portal?", animation = 568)
    chatNpc("It's supposed to lead to the Giant Island.<br>Again, no response from there either.", animation = 554)
    chatPlayer("And the western portal?", animation = 568)
    chatNpc("That one... it's strange. It shows bizarre images,<br>but I have no idea where it connects to.", animation = 554)
    chatNpc("Until I receive confirmation from the other side,<br>none of these portals can be used.", animation = 554)
    chatPlayer("Sounds like you've got your hands full.", animation = 568)
    chatNpc("Indeed. If you hear anything about these portals,<br>please let me know.", animation = 554)
}
