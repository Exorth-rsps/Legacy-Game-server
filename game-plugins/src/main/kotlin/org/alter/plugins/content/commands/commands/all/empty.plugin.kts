package org.alter.plugins.content.commands.commands.all

on_command("empty") {
    player.inventory.removeAll()
}