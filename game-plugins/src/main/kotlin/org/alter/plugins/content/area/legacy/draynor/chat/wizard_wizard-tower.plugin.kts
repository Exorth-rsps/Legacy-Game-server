package org.alter.plugins.content.area.legacy.draynor.chat

import org.alter.plugins.content.magic.TeleportType
import org.alter.plugins.content.magic.teleport

arrayOf(Npcs.WIZARD_4399).forEach { shop ->
    on_npc_option(npc = shop, option = "talk-to") { player.queue { dialog() } }
}

suspend fun QueueTask.dialog() {
    chatNpc("Can I help you?")
    val region = player.tile.regionId
    var continueDialog = true
    while (continueDialog) {
        when(player.tile.regionId) {
            12437 -> { //Seers Village
                when (options()) {
                    1 -> {
                        teleport_soulaltar()
                        continueDialog = false
                    }
                    2 -> {
                        no_thank_you()
                        continueDialog = false
                    }
                }
            } else -> {
            when (options()) {
                1 -> {
                    teleport_wizardtower()
                    continueDialog = false
                }
                2 -> {
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
    return if (region == 12437) {
        options(
            "Can you teleport me to the Soul Altar?",
            "No thank you."
        )
    } else {
        options(
            "Can you teleport me back to the wizard tower?",
            "No thank you."
        )
    }
}

suspend fun QueueTask.no_thank_you() {
    chatPlayer("No thank you.", animation = 568)
    chatNpc("Well, have a good day.", animation = 554)
}

suspend fun QueueTask.teleport_soulaltar() {
    chatPlayer("an you teleport me back to the Soul Altar?", animation = 568)
    teleport_soulaltar(player)
}
suspend fun QueueTask.teleport_wizardtower() {
    chatPlayer("an you teleport me back to the Soul Altar?", animation = 568)
    teleport_wizardtower(player)
}


fun teleport_soulaltar(p: Player) {
    p.queue {
        val npc = player.getInteractingNpc()
        player.lock = LockState.FULL
        player.teleport(type = TeleportType.MODERN, endTile = Tile (1827, 3884, 0))
        player.lock = LockState.NONE
    }
}
fun teleport_wizardtower(p: Player) {
    p.queue {
        val npc = player.getInteractingNpc()
        player.lock = LockState.FULL
        player.teleport(type = TeleportType.MODERN, endTile = Tile (3104, 9570, 0))
        player.lock = LockState.NONE
    }
}