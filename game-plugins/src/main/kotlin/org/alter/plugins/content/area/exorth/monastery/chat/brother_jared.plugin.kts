package org.alter.plugins.content.areas.monastery.chat

arrayOf(Npcs.BROTHER_JERED).forEach { npc ->

    on_npc_option(npc = npc, option = "talk-to") { player.queue { dialog() } }

}

suspend fun QueueTask.dialog() {
    chatNpc("Greetings, child of Saradomin. How may I assist you?")
    var continueDialog = true
    while (continueDialog) {
        when (options(
            "Can you heal me?",
            "Can you sell me a Prayer Skillcape?",
            "No, thank you."
        )) {
            1 -> {
                heal_request()
                continueDialog = false
            }
            2 -> {
                prayer_skillcape_request()
                continueDialog = false
            }
            3 -> {
                chatPlayer("No, thank you.", animation = 568)
                chatNpc("May Saradomin's light guide you.", animation = 554)
                continueDialog = false
            }
        }
    }
}

suspend fun QueueTask.heal_request() {
    chatPlayer("Can you heal me?", animation = 568)
    chatNpc("Of course, child. Let Saradomin's light restore your strength.", animation = 554)
    healing(player)
}

fun healing(p: Player) {
    p.queue {
        val npc = player.getInteractingNpc()
        player.lock = LockState.FULL
        npc.forceChat("By the power of Saradomin!")
        npc.graphic(108, 10)
        player.heal(player.getSkills().getMaxLevel(Skills.HITPOINTS))
        player.message("You feel completely healed.")
        player.lock = LockState.NONE
    }
}

suspend fun QueueTask.prayer_skillcape_request() {
    if (player.skillSet.getMaxLevel(Skills.PRAYER) >= 99) {
        chatPlayer("Can you sell me a Prayer Skillcape?", animation = 568)
        chatNpc("For a prayer warrior like you? It costs 99000 coins.", animation = 554)

        when (options("99000! That's much too expensive.", "I think I have the money right here.")) {
            1 -> {
                chatPlayer("99000! That's much too expensive.", animation = 568)
                chatNpc("The skillcape is a symbol of great devotion. Let me know if you change your mind.", animation = 554)
            }
            2 -> {
                chatPlayer("I think I have the money right here.", animation = 568)
                if (player.inventory.freeSlotCount < 1) {
                    chatNpc("You don't have enough free space in your inventory for the Skillcape of Prayer.")
                    return
                }
                if (player.inventory.getItemCount(Items.COINS_995) >= 99000) {
                    player.inventory.remove(Items.COINS_995, 99000)
                    player.inventory.add(Items.PRAYER_CAPE)
                    chatNpc("Wear this cape with pride, and may Saradomin's light shine upon you.")
                } else {
                    chatPlayer("Oh, I don't have enough coins.")
                    chatNpc("Come back when you do, child.")
                }
            }
        }
    } else {
        chatNpc("You must reach level 99 Prayer to earn the Skillcape of Prayer.", animation = 554)
    }
}
