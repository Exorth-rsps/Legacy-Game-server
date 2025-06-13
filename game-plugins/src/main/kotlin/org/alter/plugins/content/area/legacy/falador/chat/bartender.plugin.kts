package org.alter.plugins.content.area.legacy.falador.chat

on_npc_option(npc = Npcs.BARTENDER_1320, option = "talk-to", lineOfSightDistance = 3) {
    player.queue {
        bartenderDialogue()
    }
}

suspend fun QueueTask.bartenderDialogue() {
    chatNpc("What can I do for you?")
    when (options(
        "A glass of your finest ale please.",
        "Where an adventurer might make his fortune?",
        "Do you know where I can get some good equipment?"
    )) {
        1 -> {
            chatPlayer("A glass of your finest ale please.")
            chatNpc("No problemo. That'll be 2 coins.")
            if (player.inventory.hasFreeSpace()) {
                if (player.inventory.remove(Items.COINS_995, 2).hasSucceeded()) {
                    player.inventory.add(Items.BEER)
                    itemMessageBox("The bartender hands you a fine ale.", Items.BEER)
                } else {
                    chatNpc("You don't have enough coins in your inventory for me to sell you a beer.")
                }
            } else {
                chatNpc("You don't have enough free space in your inventory for me to sell you a beer.")
            }
        }
        2 -> {
            chatPlayer("Can you recommend where an adventurer might make his fortune?")
            chatNpc("Ooh I don't know if I should be giving away information, makes the game too easy.")
            when (options(
                "Oh ah well...",
                "Game? What are you talking about?",
                "Just a small clue?",
                "Do you know where I can get some good equipment?"
            )) {
                1 -> {
                    chatPlayer("Oh ah well...")
                }
                2 -> {
                    chatPlayer("Game? What are you talking about?")
                    chatNpc("This world around us... is an online game... called Exorth.")
                    chatPlayer("Nope, still don't understand what you are talking about. What does 'online' mean?")
                    chatNpc(
                        "It's a sort of connection between magic boxes across the world, big boxes on people's desktops and little ones people can carry. They talk to each other to play games."
                    )
                    chatPlayer("I give up. You're obviously completely mad!")
                }
                3 -> {
                    chatPlayer("Just a small clue?")
                    chatNpc("No i dont give you one clue!")
                }
                4 -> {
                    chatPlayer("Do you know where I can get some good equipment?")
                    chatNpc("Well, there are some shop across Falador.")
                }
            }
        }
        3 -> {
            chatPlayer("Do you know where I can get some good equipment?")
            chatNpc("Well, there are some shop across Falador.")
        }
    }
}
