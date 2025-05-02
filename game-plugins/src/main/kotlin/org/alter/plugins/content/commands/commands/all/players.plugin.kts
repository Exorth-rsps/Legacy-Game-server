package org.alter.plugins.content.commands.commands.all

import org.alter.game.model.entity.Player

private val LIST_IFACE  = 187
private val LIST_SCRIPT = 217

on_command("players", description = "Show online players in interface") {
    // 1) Haal je playerLimit uit de GameContext
    val limit = world.gameContext.playerLimit

    // 2) Loop van 0 tot limit en verzamel alle niet-null spelers
    val names = mutableListOf<String>()
    for (i in 0 until limit) {
        val p: Player? = player.world.players[i]  // returns null als slot leeg
        if (p != null) {
            names += p.username.replace(" ", "<col=ffffff>·</col>")
        }
    }

    // 3) Open je interface (ID 187)
    player.setInterfaceUnderlay(color = -1, transparency = -1)
    player.openInterface(LIST_IFACE, dest = InterfaceDestination.MAIN_SCREEN)

    // 4) Vul ‘m met titel + je |-gescheiden lijst via script 217
    val title = "Players Online (${names.size})"
    val data  = names.joinToString("|")
    player.runClientScript(LIST_SCRIPT, title, data)
    player.message("There are ${names.size} players online!")
}
