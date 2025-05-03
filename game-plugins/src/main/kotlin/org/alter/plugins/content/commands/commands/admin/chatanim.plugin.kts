package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.priv.Privilege


on_command("chatanim", Privilege.ADMIN_POWER, description = "Chat dialogue test") {
    val args = player.getCommandArgs()
    val key = args[0].toInt()
    val npcId = args[1].toInt()

    player.queue {
        chatNpc("Hello World", npcId, animation = key, "hi")
    }
    player.message("$key opened in a dialog", ChatMessageType.ENGINE)
}


