package org.alter.plugins.content.area.yanille.chat

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
                question1()
                continueDialog = true
            }
            3 -> {
                no_thank_you()
                continueDialog = false
            }
        }
    }
}

suspend fun QueueTask.options(): Int = options(
    "Can you teleport me to the Rune Essence Mine?",
    "What is this place?",
    "No thank you."
)

suspend fun QueueTask.no_thank_you() {
    chatPlayer("No thank you.", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
}
suspend fun QueueTask.question1() {
    chatPlayer("What is this place?", animation = 568)
    chatNpc("Welcome to the Magic Guild.<br>A place where allot of magic happens.", animation = 554)
    chatPlayer("Oh wow, Magic!<br>What can i find here?", animation = 568)
    chatNpc("In the basement you can find some zombies locked in a cage!", animation = 554)
    chatNpc("Here at the ground floor you find me...<br> I can send you to the rune essence mines.", animation = 554)
    chatNpc("At the first floor, you find Wizard Akutha.<br>He sells some Magic Supplies in his Magic Guild Store.", animation = 554)
    chatNpc("At the top you find Zavistic Rave, he is doing something with portal teleportation...", animation = 554)
    chatPlayer("Well im gonna check out those things!", animation = 568)

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
        player.moveTo(2912, 4833, 0)
        player.lock = LockState.NONE
    }
}
