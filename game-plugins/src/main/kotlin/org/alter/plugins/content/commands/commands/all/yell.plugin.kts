package gg.rsmod.plugins.content.commands

// Houdt voor elke speler het tijdstip van de laatste 'yell' bij
private val lastYellTime = mutableMapOf<Player, Long>()

on_command("yell", description = "Yell to everyone") {
    val now = System.currentTimeMillis()

    // Alleen voor gewone spelers (privilege.id == 0) een cooldown van 15s, voor anderen geen cooldown
    val cooldownMs = if (player.privilege.id == 0) 15_000L else 0L
    val last = lastYellTime[player] ?: 0L

    // Cooldown-check (zal voor admins e.d. nooit triggeren omdat cooldownMs == 0)
    if (now - last < cooldownMs) {
        val secondsLeft = ((cooldownMs - (now - last)) / 1000) + 1
        player.message(
            "You need to wait $secondsLeft second${if (secondsLeft > 1) "s" else ""} before your next yell!.",
            ChatMessageType.CONSOLE
        )
        return@on_command
    }

    // Update timestamp
    lastYellTime[player] = now

    // Bepaal rank en kleur aan de hand van privilege.id
    val (rank, color) = when (player.privilege.id) {
        0 -> "Player" to ""
        1 -> "<img=0>Moderator" to "<col=8900331>"
        2 -> "<img=1>Admin" to "<col=8900331>"
        3 -> "<img=1>Admin" to "<col=8900331>"
        else -> "unidentified" to ""
    }

    // Maak de gebruikersnaam zichtbaar met middelpunt in plaats van spatie
    val visibleName = player.username.replace(" ", "<col=ffffff>·</col>")

    // Combineer alle argumenten tot één string
    val text = player.getCommandArgs().joinToString(" ")

    // Stuur het bericht naar alle spelers in de wereld
    player.world.players.forEach {
        it.message("$color[$rank] $visibleName: $text", ChatMessageType.CONSOLE)
    }
}
