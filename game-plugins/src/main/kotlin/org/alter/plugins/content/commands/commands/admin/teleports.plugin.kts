package org.alter.plugins.content.commands.commands.admin

import org.alter.game.model.priv.Privilege
import org.alter.plugins.content.magic.TeleportType
import org.alter.plugins.content.magic.canTeleport
import org.alter.plugins.content.magic.teleport


on_command("varrock", Privilege.ADMIN_POWER, description = "Teleports you to Varrock") {
    player.moveTo(Tile(x = 3211, z = 3424, height = 0))
}
on_command("falador", Privilege.ADMIN_POWER, description = "Teleports you to Falador") {
    player.moveTo(Tile(x = 2966, z = 3379, height = 0))
}
on_command("lumbridge", Privilege.ADMIN_POWER, description = "Teleports you to Lumbridge") {
    player.moveTo(Tile(x = 3222, z = 3217, height = 0))
}
on_command("yanille", Privilege.ADMIN_POWER, description = "Teleports you to Yanille") {
    player.moveTo(Tile(x = 2606, z = 3093, height = 0))
}
on_command("gnome", Privilege.ADMIN_POWER, description = "Teleports you to Gnome Stronghold") {
    player.moveTo(Tile(x = 2461, z = 3443, height = 0))
}
on_command("essence", Privilege.ADMIN_POWER, description = "Teleports you to Essence Mine") {
    player.moveTo(Tile(x = 2898, z = 4818, height = 0))
}

//Exorth teleports
on_command("landsend", Privilege.ADMIN_POWER, description = "Teleports you to Lands End") {
    player.moveTo(Tile(x = 2550, z = 1766, height = 0))
}
on_command("seers", Privilege.ADMIN_POWER, description = "Teleports you to Seers Village") {
    player.moveTo(Tile(x = 2762, z = 1725, height = 0))
}
on_command("witchaven", Privilege.ADMIN_POWER, description = "Teleports you to Witchaven") {
    player.moveTo(Tile(x = 2724, z = 1538, height = 0))
}
on_command("catherby", Privilege.ADMIN_POWER, description = "Teleports you to Catherby") {
    player.moveTo(Tile(x = 2566, z = 1524, height = 0))
}
