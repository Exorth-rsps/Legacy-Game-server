package org.alter.plugins.content.area.exorth.landsend.chat

on_npc_option(npc = Npcs.LAN_THE_BUTCHER, option = "talk-to", lineOfSightDistance = 1) { player.queue { dialog() } }

suspend fun QueueTask.dialog() {
    chatNpc("What d'ya want?", animation = 554)
    chatPlayer("Just exploring the far reaches of this wonderful land.", animation = 568)
    chatNpc("Why?", animation = 554)
    chatPlayer("For gold! For glory!", animation = 568)
    chatNpc("You got gold?", animation = 554)
    chatPlayer("Maybe a little...", animation = 568)
    chatNpc("Well, I'm a butcher, I'll be 'appy to perhaps trade you, that I would.", animation = 554)
    chatPlayer("I don't see any product around here...", animation = 568)
    chatNpc("Heh, o' course not. I keep it down in the basement, that I do.", animation = 554)
    chatPlayer("What basement?", animation = 568)
    chatNpc("...", animation = 554)
    chatPlayer("...", animation = 568)
    chatNpc("I think I'll be leaving now.", animation = 554)
    chatPlayer("Heh heh heh, that you should.", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)

}
