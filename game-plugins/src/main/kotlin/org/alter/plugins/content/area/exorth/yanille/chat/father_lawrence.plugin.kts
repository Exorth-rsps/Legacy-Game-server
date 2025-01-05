package org.alter.plugins.content.area.yanille.chat

arrayOf(Npcs.FATHER_LAWRENCE).forEach { npc ->
    on_npc_option(npc = npc, option = "talk-to", lineOfSightDistance = 1) { player.queue { dialog() } }
}

suspend fun QueueTask.dialog() {
    chatNpc("Ah, child of Saradomin, welcome to our humble chapel.")
    var continueDialog = true
    while (continueDialog) {
        when (options()) {
            1 -> {
                ask_about_chapel()
                continueDialog = true
            }
            2 -> {
                ask_about_background()
                continueDialog = true
            }
            3 -> {
                ask_about_healing()
                continueDialog = false
            }
            4 -> {
                no_thank_you()
                continueDialog = false
            }
        }
    }
}

suspend fun QueueTask.options(): Int = options(
    "Tell me about this chapel.",
    "What is your story?",
    "Can you heal me?",
    "No thank you."
)

suspend fun QueueTask.no_thank_you() {
    chatPlayer("No thank you.", animation = 568)
    chatNpc("May Saradomin's light guide you, always.", animation = 554)
}

suspend fun QueueTask.ask_about_chapel() {
    chatPlayer("Tell me about this chapel.", animation = 568)
    chatNpc("This chapel is dedicated to Saradomin, the bringer of light and order.", animation = 554)
    chatNpc("I devote all my time and energy to maintaining this sacred place.", animation = 554)
    chatPlayer("You must be very dedicated to Saradomin.", animation = 568)
    chatNpc("Indeed, child. Saradomin's will is my purpose, and through prayer, I serve him.", animation = 554)
}

suspend fun QueueTask.ask_about_background() {
    chatPlayer("What is your story?", animation = 568)
    chatNpc("I was trained at the monastery west of Yanille by the esteemed Brother Jered.", animation = 554)
    chatNpc("It was there I learned the ways of Saradomin and the art of healing.", animation = 554)
    chatPlayer("Impressive! You must be quite skilled.", animation = 568)
    chatNpc("Saradomin has blessed Brother Jered as the bearer of the prayer cape.<br>A symbol of devotion and a reminder of his duty.", animation = 554)
    chatPlayer("He must be a great example to others.", animation = 568)
    chatNpc("He strives to be, child. Saradomin's light shines through all who follow him.", animation = 554)
}

suspend fun QueueTask.ask_about_healing() {
    chatPlayer("Can you heal me?", animation = 568)
    chatNpc("Of course, child. Saradomin's light is a beacon of restoration.", animation = 554)
    chatNpc("Let me call upon his blessing to heal your wounds.", animation = 554)
    healing(player)

}
fun healing(p: Player) {
    p.queue {
        val npc = player.getInteractingNpc()
        player.lock = LockState.FULL
        npc.forceChat("By the power of Saradomin!")
        npc.graphic(108, 10)
        player.heal(4 + ((player.getSkills().getCurrentLevel(Skills.HITPOINTS) * 0.12) * 10).toInt())
        player.message("You feel a little better.")
        player.lock = LockState.NONE
    }
}