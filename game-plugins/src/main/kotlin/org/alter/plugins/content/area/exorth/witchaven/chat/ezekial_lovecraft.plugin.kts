package org.alter.plugins.content.areas.yanille.chat

arrayOf(Npcs.EZEKIAL_LOVECRAFT).forEach { shop ->

    on_npc_option(npc = shop, option = "talk-to") { player.queue { dialog() } }

    on_npc_option(npc = shop, option = "shop") { open_shop(player) }
}

suspend fun QueueTask.dialog() {
    chatNpc("Well hello there<br> welcome to our little village.<br> Pray, stay awhile.")
    when (options()) {
        1 -> question_1()
        2 -> question_2()
        3 -> question_3()
        4 -> question_4()
    }
}

suspend fun QueueTask.options(): Int = options("You know, you sound just like the mayor?", "What do you do here?", "Can I buy some fishing supplies please?", "You are a bit too strange for me. Bye.")

suspend fun QueueTask.question_1() {
    chatPlayer("You know, you sound just like the mayor?", animation = 568)
    chatNpc("Really? That's the nicest thing anyone has ever said.", animation = 554)
    chatNpc("I admire him a great deal. He works so hard for this town.", animation = 554)
    chatPlayer("You look like him as well?", animation = 568)
    chatNpc("Hmm, I don't think I do.", animation = 554)
    chatPlayer("No really, what with the unhealthy skin tone and bulging eyes. <br>The resemblance is uncanny.", animation = 568)
    chatNpc("Well, I haven't spoken to him in a while. I don't get out much.", animation = 554)
    chatPlayer("I can see why.", animation = 568)
    chatNpc("Maybe the Mayor has finally seen how much I admire him and has grown to admire me?", animation = 554)
    chatPlayer("Anything's possible I suppose. <br>Wait a minute, you mean you've always looked that odd?", animation = 568)
    chatNpc("Hey, I resent that!", animation = 554)
    chatPlayer("I'm sorry, it's just... <br>Nevermind.", animation = 568)
}
suspend fun QueueTask.question_2() {
    chatPlayer("What do you do here?", animation = 568)
    chatNpc("I supply the local fishermen with the tools and bait they need to do their job.", animation = 554)
    chatPlayer("Interesting. Have you been doing it long?", animation = 568)
    chatNpc("Why yes, all of my life. I took over from my father, who inherited the business from his father and so on.", animation = 554)
    chatNpc("In fact, there have been Lovecraft's selling bait for over ten generations.", animation = 554)
    chatPlayer("Wow, that's some lineage.", animation = 568)
    chatNpc("Oh yes, we have a long and interesting family history. For one reason or another the Lovecraft's have always been bait sellers or writers.", animation = 554)
    chatPlayer("Hmm, yes well. I'm sure it's all fascinating, but I...", animation = 568)
    chatNpc("Oh very, very fascinating, for instance my great grandfather Howard...", animation = 554)
    chatPlayer("Yawn. Oh, I'm sorry but I really must be getting on. I think my giraffe needs feeding.", animation = 568)
    chatNpc("Your?", animation = 554)
    chatPlayer("Giraffe. Sorry, but he gets cranky without enough sugar. Bye now!", animation = 568)
    chatNpc("Oh right, goodbye.", animation = 554)
}
suspend fun QueueTask.question_3() {
    chatPlayer("Can I buy some fishing supplies please?", animation = 568)
    open_shop(player)
}
suspend fun QueueTask.question_4() {
    chatPlayer("You are a bit too strange for me. Bye.", animation = 568)
    chatNpc("Sniff. Yes, everyone says that.", animation = 554)
}

fun open_shop(p: Player) {
    p.openShop("Lovecraft's Tackle")
}