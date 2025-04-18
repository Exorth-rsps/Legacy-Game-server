package org.alter.plugins.content.area.legacy.dwarven_mine.chat

arrayOf(Npcs.BELONA).forEach { shop ->

    on_npc_option(npc = shop, option = "talk-to", lineOfSightDistance = 1) { player.queue { dialog() } }
    on_npc_option(npc = shop, option = "trade") { open_shop(player) }
}

    suspend fun QueueTask.dialog() {
        chatNpc("Would you like to exchange unidentified minerals?")
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
        "Yes please.",
        "No thank you.",
    )

    suspend fun QueueTask.question_1() {
        chatPlayer("Yes please.", animation = 568)
        open_shop(player)
    }

    suspend fun QueueTask.question_2() {
        chatPlayer("No thank you.", animation = 568)
    }

fun open_shop(p: Player) {
    p.openShop("Mining Guild Mineral Exchange")
}