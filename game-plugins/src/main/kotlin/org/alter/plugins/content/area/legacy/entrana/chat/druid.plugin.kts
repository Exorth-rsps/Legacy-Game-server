package org.alter.plugins.content.area.legacy.entrana.chat


arrayOf(Npcs.DRUID, Npcs.UNGADULU_3958, Npcs.UNGADULU).forEach { shop ->
    on_npc_option(npc = shop, option = "talk-to") { player.queue { randomDialog() } }
}


suspend fun QueueTask.dialog1() {
    chatPlayer("Hello.")
    chatNpc("Good day to you, may nature smile upon you.")
}

suspend fun QueueTask.dialog2() {
    chatNpc("Nature provides healing, if you know where to look.")
    chatPlayer("Which herbs do you recommend for beginners?")
    chatNpc("Start with guam. Simple, yet powerful.")
    chatPlayer("Thanks, I'll keep an eye out.")
}
suspend fun QueueTask.dialog3() {
    chatNpc("The balance of nature is fragile.")
    chatPlayer("How can I help protect it?")
    chatNpc("Respect its gifts and never take more than you need.")
    chatPlayer("I'll do my best.")
}
suspend fun QueueTask.dialog4() {
    chatNpc("We druids seek peace and harmony.")
    chatPlayer("How do you find calm in a chaotic world?")
    chatNpc("By listening to the trees and the wind.")
    chatPlayer("Maybe I should try that too.")
}
suspend fun QueueTask.dialog5() {
    chatNpc("Our magic stems from the ancient oaks.")
    chatPlayer("How do you communicate with a tree?")
    chatNpc("With respect, silence, and much patience.")
    chatPlayer("Sounds like a challenge!")
}
suspend fun QueueTask.dialog6() {
    chatNpc("I see a dark shadow in your future.")
    chatPlayer("That doesn’t sound good. What should I do?")
    chatNpc("Follow the light of your inner compass.")
    chatPlayer("I hope it guides me in time.")
}

suspend fun QueueTask.randomDialog() {
    val dialogs = listOf<suspend QueueTask.() -> Unit>(
        { dialog1() },
        { dialog2() },
        { dialog3() },
        { dialog4() },
        { dialog5() },
        { dialog6() }
    )
    dialogs.random().invoke(this)
}