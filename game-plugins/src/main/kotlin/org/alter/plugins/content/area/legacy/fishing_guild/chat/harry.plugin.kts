package org.alter.plugins.content.areas.exorth.catherby.chat

arrayOf(Npcs.HARRY).forEach { shop ->

    on_npc_option(npc = shop, option = "talk-to") { player.queue { dialog() } }

    on_npc_option(npc = shop, option = "trade") { open_shop(player) }
}

suspend fun QueueTask.dialog() {
    chatNpc("Welcome, you can buy fishing equipment at my store. We'll also give you a good price for any fish that you catch.")
    var continueDialog = true
    while (continueDialog) {
        when (options()) {
            1 -> question_1()
            2 -> {
                question_2()
                continueDialog = false
            }
        }
    }
}

suspend fun QueueTask.options(): Int = options(
    "Let's see what you've got then.",
    "No, I prefer to bash things close up.",
)

suspend fun QueueTask.question_1() {
    chatPlayer("Let's see what you've got then.", animation = 568)
    open_shop(player)
}

suspend fun QueueTask.question_2() {
    chatPlayer("Sorry, I'm not interested.", animation = 568)
}


fun open_shop(p: Player) {
    p.openShop("Harry's Fishing Shop")
}