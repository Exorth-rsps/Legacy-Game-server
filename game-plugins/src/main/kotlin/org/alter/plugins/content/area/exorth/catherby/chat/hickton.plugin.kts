package org.alter.plugins.content.areas.exorth.catherby.chat

arrayOf(Npcs.HICKTON).forEach { shop ->

    on_npc_option(npc = shop, option = "talk-to") { player.queue { dialog() } }

    on_npc_option(npc = shop, option = "trade") { open_shop(player) }
}

suspend fun QueueTask.dialog() {
    chatNpc("Welcome to Hickton's Archery Emporium. <br>Do you want to see my wares?")
    var continueDialog = true
    while (continueDialog) {
        when (options()) {
            1 -> question_1()
            2 -> question_2()
            3 -> {
                question_3()
                continueDialog = false
            }
        }
    }
}

suspend fun QueueTask.options(): Int = options(
    "Yes, please.",
    "Can you sell me a Fletching Skillcape?",
    "No, I prefer to bash things close up.",
)

suspend fun QueueTask.question_1() {
    chatPlayer("Yes, please", animation = 568)
    open_shop(player)
}

suspend fun QueueTask.question_2() {
    if (player.skillSet.getMaxLevel(Skills.FLETCHING) >= 99) {
        chatPlayer("Can you sell me a Fletching Skillcape?", animation = 568)
        chatNpc("For a fletcher of your calibre? I'm afraid such things<br>do not come cheaply. They cost 99000 coins, to be precise!", animation = 554)

        when (options("99000! That's much too expensive.", "I think I have the money right here, actually.")) {
            1 -> {
                chatPlayer("99000! That's much too expensive.", animation = 568)
                chatNpc("Not at all; there are many other adventurers who would love the opportunity to purchase such a prestigious item! You can find me here if you change your mind.", animation = 554)
            }
            2 -> {
                chatPlayer("I think I have the money right here, actually.")
                if (player.inventory.freeSlotCount < 2) {
                    chatNpc(
                        "You don't have enough free space in your inventory for me to sell you a Skillcape of Fletching.",
                    )
                    chatNpc("Come back to me when you've cleared up some space.")
                    return
                }
                if (player.inventory.getItemCount(Items.COINS_995) >= 99000) {
                    player.inventory.add (Items.FLETCHING_CAPE)
                    chatNpc("Excellent! Wear that cape with pride my friend.")
                } else {
                    chatPlayer("But, unfortunately, I was mistaken.")
                    chatNpc("Well, come back and see me when you do.")
                }
            }
        }
    } else {
        chatNpc("Unfortunately I cannot sell you a Skillcape of Fletching yet, as you don't meet the requirements to wear it. Feel free to take a look at my other wares, though.", animation = 554)
    }
}


suspend fun QueueTask.question_3() {
    chatPlayer("No, I prefer to bash things close up.", animation = 568)
}


fun open_shop(p: Player) {
    p.openShop("Hickton's Archery Emporium")
}