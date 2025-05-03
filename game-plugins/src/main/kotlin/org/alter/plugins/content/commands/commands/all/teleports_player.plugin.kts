package org.alter.plugins.content.commands.commands.all


on_command("home", description = "Teleports you home") {
    val home = world.gameContext.home
    player.moveTo(home)
}
