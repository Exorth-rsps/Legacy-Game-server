package org.alter.plugins.content.area.exorth.mainlands.chat

val barbarianLadderNpcId = Npcs.HUNDING
on_npc_option(npc = barbarianLadderNpcId, option = "talk-to") { player.queue { barbarianDungeonDialogue() } }

suspend fun QueueTask.barbarianDungeonDialogue() {
    chatNpc("Halt, outsider. What brings you to this ladder?")
    chatPlayer("I was curious. What's down there?")
    chatNpc("Deep below lies a moss-covered monster, <br>a creature of great strength.")
    chatPlayer("That sounds dangerous. Should I be worried?")
    chatNpc("Not unless you're foolish enough to go unprepared. <br>The beast is safely locked away.")
    chatNpc("You'll need a Mossy Key to open the gate and face it.")
    chatPlayer("Where can I find this Mossy Key?")
    chatNpc("The key is rare, <br>found only by those brave enough to defeat creatures in the wild.")
    chatPlayer("Thanks for the warning. I'll make sure to be prepared.")
    chatNpc("Good. Remember, <br>strength and courage are the only things that matter down there.")
}