package org.alter.plugins.content.areas.landsend.chat


arrayOf(Npcs.HUGOR).forEach { shop ->
    on_npc_option(npc = shop, option = "talk-to") { player.queue { randomDialog() } }
}


suspend fun QueueTask.dialog1() {
    chatNpc("Got anymore *hic* wine?")
    chatPlayer("Ummm... No, sorry. Looks like you've had enough anyway.")
    chatNpc("Well, I *hic* ...disagree.")
    chatPlayer("Why are you drinking so much? To forget?")
    chatNpc("Nope, tastes mighty fine! Any chance you *hic* know where Horses go?")
    chatPlayer("The what?")
    chatNpc("The Horses!")
    chatPlayer("Ah, the 'Horses'... How unfortunate, the wine has made you delirious. I've no idea what a Horse is my friend.")
    chatNpc("Oh.")
}

suspend fun QueueTask.dialog2() {
    chatNpc("You know this *hic* place was built twice?")
    chatPlayer("Ummm... How come?")
    chatNpc("Storm swept in and erased it, had to be built again from scratch.")
    chatPlayer("Huh, what a shame.")
    chatNpc("They did a better job second time round, but the builder sure was annoyed.")
    chatPlayer("Who was the builder?")
    chatNpc("Some *hic* Westener.")
}


suspend fun QueueTask.randomDialog() {
    val dialogs = listOf<suspend QueueTask.() -> Unit>(
        { dialog1() },
        { dialog2() }
    )
    dialogs.random().invoke(this)
}
