on_command("yell", description = "Yell to everyone") {

    val rank: String
    val color: String

    when (player.privilege.id) {
        0 -> {
            rank = "Player"
            color = ""
        }
        1 -> {
            rank = "<img=0>Moderator"
            color = "<col=8900331>"
        }
        2 -> {
            rank = "<img=1>Admin"
            color = "<col=8900331>"
        }
        3 -> {
            rank = "<img=1>Developer"
            color = "<col=8900331>"
        }
        4 -> {
            rank = "<img=1>Owner"
            color = "<col=8900331>"
        }
        5 -> {
            rank = "<img=8>Donator"
            color = "<col=8900331>"
        }
        else -> {
            rank = "unidentified"
            color = ""
        }
    }

    // Combineer alle argumenten in één string
    val args = player.getCommandArgs()
    val name = player.username
    val text = args.joinToString(" ") // Voegt alle woorden samen met een spatie ertussen

    // Stuur het yell-bericht naar alle spelers
    player.world.players.forEach {
        it.message("${color}[${rank}] ${name}: ${text}", ChatMessageType.CONSOLE)
    }
}
