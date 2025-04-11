package org.alter.plugins.content.areas.legacy.falador.chat

arrayOf(Npcs.MAN_3106, Npcs.MAN_3107, Npcs.WOMAN_3111, Npcs.WOMAN_3112).forEach { npc ->
    on_npc_option(npc = npc, option = "talk-to") { player.queue { randomDialog() } }
}

suspend fun QueueTask.dialog1() {
    chatPlayer("Hello there.")
    chatNpc("Greetings, traveler. What brings you to these parts?")
    chatPlayer("Just passing through, enjoying the scenery.")
    chatNpc("Enjoy it while you can; the world is ever-changing.")
}

suspend fun QueueTask.dialog2() {
    chatNpc("Have you seen the sunrise from the hills?")
    chatPlayer("No, I haven't. Is it special?")
    chatNpc("It's a sight that reminds you why the gods gave us this land.")
    chatPlayer("I'll make sure to catch it someday.")
}

suspend fun QueueTask.dialog3() {
    chatPlayer("What do you do around here?")
    chatNpc("Oh, a bit of this and that. Keeping the village running.")
    chatPlayer("Sounds busy. Do you enjoy it?")
    chatNpc("Hard work keeps the mind sharp and the heart content.")
}

suspend fun QueueTask.dialog4() {
    chatNpc("Be careful when venturing out. Not all paths are safe.")
    chatPlayer("Thanks for the warning. Are there many dangers nearby?")
    chatNpc("Wolves, bandits, and the occasional stray spell.<br>Stay alert.")
    chatPlayer("I'll keep that in mind. Thanks.")
}

suspend fun QueueTask.dialog5() {
    chatNpc("Did you hear about the merchant's lost caravan?")
    chatPlayer("No, what happened?")
    chatNpc("Vanished without a trace. Some say it's cursed.")
    chatPlayer("Sounds like trouble. I'll stay away from cursed goods.")
}

suspend fun QueueTask.dialog6() {
    chatPlayer("What's the news around here?")
    chatNpc("Not much, just the usual village gossip and farm troubles.")
    chatPlayer("Anything interesting?")
    chatNpc("If you find sheep wandering about, let the shepherd know!")
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
