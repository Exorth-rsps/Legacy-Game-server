package org.alter.plugins.content.area.yanille.chat

on_npc_option(npc = Npcs.RADIMUS_ERKLE, option = "talk-to", lineOfSightDistance = 1) { player.queue { dialog() } }

suspend fun QueueTask.dialog() {
    chatNpc("Hello and welcome to the legends guild, <br> how can I help you.")
    var continueDialog = true
    while (continueDialog) {
        when (options()) {
            1 -> question_1()
            2 -> question_2()
            3 -> question_3()
            4 -> question_4()
            5 -> {
                question_5()
                continueDialog = false
            }
        }
    }
}

suspend fun QueueTask.options(): Int = options(
    "Who are you?",
    "What can I find here?",
    "Can I buy anything here?",
    "Those stairs leading down... what's there?",
    "No, thank you."
)

suspend fun QueueTask.question_1() {
    chatPlayer("Who are you?", animation = 568)
    chatNpc("My name is Radimus Erkle, the keeper of the Legends Guild. I have honored this place for many years. How can I help you?", animation = 554)
}

suspend fun QueueTask.question_2() {
    chatPlayer("What can I find here?", animation = 568)
    chatNpc("In the southern building, you can find a Bank, but as you've surely noticed, we have multiple floors, which I can tell you about as well. ", animation = 554)
    chatNpc("Additionally, in the eastern tower, you'll find Bank Chests.", animation = 554)
}

suspend fun QueueTask.question_3() {
    chatPlayer("Can I buy anything here?", animation = 568)
    chatNpc("Of course! On the ground floor, you can find Fionella and her General Store, and at the top of the western tower is my brother Siegfried, who also sells many useful items.", animation = 554)
}

suspend fun QueueTask.question_4() {
    chatPlayer("Those stairs leading down... <br>what's there?", animation = 568)
    chatNpc("I’d rather not talk about it.", animation = 554)
    chatNpc("I must admit, I feel a bit of fear when I hear the rumors about the horrors that happen down there.", animation = 554)
}

suspend fun QueueTask.question_5() {
    chatPlayer("No, thank you.", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
}
