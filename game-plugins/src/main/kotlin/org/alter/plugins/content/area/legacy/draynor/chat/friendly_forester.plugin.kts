package org.alter.plugins.content.area.legacy.draynor.chat

import org.alter.plugins.content.magic.TeleportType
import org.alter.plugins.content.magic.teleport

arrayOf(Npcs.FRIENDLY_FORESTER_11427).forEach { shop ->
    on_npc_option(npc = shop, option = "talk-to") { player.queue { dialog() } }
    on_npc_option(npc = shop, option = "shop") { open_shop(player) }
}

suspend fun QueueTask.dialog() {
    chatNpc("Can I help you?")
    val region = player.tile.regionId
    var continueDialog = true
    while (continueDialog) {
        when(player.tile.regionId) {
            10806 -> { //Seers Village
                when (options()) {
                    1 -> {
                        teleport_draynor()
                        continueDialog = false
                    }
                    2 -> {
                        teleport_wcguild()
                        continueDialog = false
                    }
                    3 -> {
                        shop()
                        continueDialog = false
                    }
                    4 -> {
                        no_thank_you()
                        continueDialog = false
                    }
                }
            } else -> {
            when (options()) {
                1 -> {
                    teleport_seers()
                    continueDialog = false
                }
                2 -> {
                    teleport_wcguild()
                    continueDialog = false
                }
                3 -> {
                    shop()
                    continueDialog = false
                }
                4 -> {
                    no_thank_you()
                    continueDialog = false
                }
            }
            }
        }
    }
}
suspend fun QueueTask.options(): Int {
    val region = player.tile.regionId
    return if (region == 10806) {
        options(
            "Can you teleport me to Draynor Village to cut down trees?",
            "Can you teleport me to the Woodcutting Guild to cut down trees?",
            "Can i check out your shop?",
            "No thank you."
        )
    } else {
        options(
            "Can you teleport me to Seers Village to cut down trees?",
            "Can you teleport me to the Woodcutting Guild to cut down trees?",
            "Can i check out your shop?",
            "No thank you."
        )
    }
}

suspend fun QueueTask.no_thank_you() {
    chatPlayer("No thank you.", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
}

suspend fun QueueTask.teleport_seers() {
    chatPlayer("Can you teleport me to Seers Village to cut down trees?", animation = 568)
    teleportseers(player)
}
suspend fun QueueTask.teleport_draynor() {
    chatPlayer("Can you teleport me to Draynor Village to cut down trees?", animation = 568)
    teleportdraynor(player)
}
suspend fun QueueTask.teleport_wcguild() {
    val wcLevel = player.getSkills().getCurrentLevel(Skills.WOODCUTTING)
    if (wcLevel < 60) {
        chatPlayer("Can you teleport me to Woodcutting Guild to cut down trees?", animation = 568)
        chatNpc("You need at least level 60 Woodcutting to enter the Guild.", animation = 568)
        return
    }
    chatPlayer("Can you teleport me to Woodcutting Guild to cut down trees?", animation = 568)
    teleportwcguild(player)
}
suspend fun QueueTask.shop() {
    chatPlayer("Can i check out your shop?", animation = 568)
    chatNpc("Sure, i sell the greatest woodcutting gear!.", animation = 568)
    open_shop(player)
}

fun teleportseers(p: Player) {
    p.queue {
        val npc = player.getInteractingNpc()
        player.lock = LockState.FULL
        npc.forceChat("Chop Chop Crack them Forester!")
        wait(3)
        player.teleport(type = TeleportType.MODERN, endTile = Tile (2725, 3485, 0))
        player.lock = LockState.NONE
    }
}
fun teleportdraynor(p: Player) {
    p.queue {
        val npc = player.getInteractingNpc()
        player.lock = LockState.FULL
        npc.forceChat("Chop Chop Chop them Forester!")
        wait(3)
        player.teleport(type = TeleportType.MODERN, endTile = Tile (3095, 3238, 0))
        player.lock = LockState.NONE
    }
}
fun teleportwcguild(p: Player) {
    p.queue {
        val npc = player.getInteractingNpc()
        player.lock = LockState.FULL
        npc.forceChat("Chop Crack Chop them Forester!")
        wait(3)
        player.teleport(type = TeleportType.MODERN, endTile = Tile (1655, 3504, 0))
        player.lock = LockState.NONE
    }
}
fun open_shop(p: Player) {
    p.openShop("Forester Store")
}