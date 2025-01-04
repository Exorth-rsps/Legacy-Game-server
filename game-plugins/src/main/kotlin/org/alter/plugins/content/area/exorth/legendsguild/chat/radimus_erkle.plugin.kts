package org.alter.plugins.content.area.yanille.chat

on_npc_option(npc = Npcs.RADIMUS_ERKLE, option = "talk-to", lineOfSightDistance = 1) { player.queue { dialog() } }

suspend fun QueueTask.dialog() {
    chatNpc("Hello and welcome to the legends guild, <br> how can i help you.")
    when (options()) {
        1 -> question_1()
        2 -> question_2()
        3 -> question_3()
        4 -> question_4()
        5 -> question_5()
    }
}

suspend fun QueueTask.options(): Int = options("Question 1", "Question 2", "Question 3", "Question 4", "Question 5")

suspend fun QueueTask.question_1() {
    chatPlayer("Question 1", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
    chatPlayer("Question 1", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
}
suspend fun QueueTask.question_2() {
    chatPlayer("Question 2", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
    chatPlayer("Question 2", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
}
suspend fun QueueTask.question_3() {
    chatPlayer("Question 3", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
    chatPlayer("Question 3", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
}
suspend fun QueueTask.question_4() {
    chatPlayer("Question 4", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
    chatPlayer("Question 4", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
}
suspend fun QueueTask.question_5() {
    chatPlayer("Question 5", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
    chatPlayer("Question 5", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
}