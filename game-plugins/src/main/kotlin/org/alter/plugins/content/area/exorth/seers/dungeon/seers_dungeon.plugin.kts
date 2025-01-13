package gg.rsmod.plugins.content.areas.exorth.seers.dungeon

import org.alter.api.InterfaceDestination
import org.alter.api.cfg.Items
import org.alter.api.cfg.Objs
import org.alter.api.ext.openInterface

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */


on_obj_option(obj = Objs.ODD_LOOKING_WALL_26115, option = "Open", lineOfSightDistance = 1) {
    if (player.tile.x == 2772 && player.tile.z == 1748) {
        if (!player.inventory.contains(Items.GIANT_KEY)) {
            player.message("You need a key to open this wall.")
            return@on_obj_option
        }
        player.moveTo(2773, 1748, player.tile.height)
    } else if (player.tile.x == 2773 && player.tile.z == 1748) {
        player.moveTo(2772, 1748, player.tile.height)
    } else {
        player.message("Nothing interesting happens.")
    }
}
on_obj_option(obj = Objs.ODD_LOOKING_WALL_26114, option = "Open", lineOfSightDistance = 1) {
    if (player.tile.x == 2772 && player.tile.z == 1747) {
        if (!player.inventory.contains(Items.GIANT_KEY)) {
            player.message("You need a key to open this wall.")
            return@on_obj_option
        }
        player.moveTo(2773, 1747, player.tile.height)
    } else if (player.tile.x == 2773 && player.tile.z == 1747) {
        player.moveTo(2772, 1747, player.tile.height)
    } else {
        player.message("Nothing interesting happens.")
    }
}

on_obj_option(obj = Objs.GATE_29486, option = "Open", lineOfSightDistance = 1) {
    player.queue {
        player.openInterface(interfaceId = 97, dest = InterfaceDestination.MAIN_SCREEN)
        player.message("Interface 97")
        wait(10)
        player.closeInterface (interfaceId =97)
        player.message("Close Interface 97")
        wait(10)
        player.openInterface(interfaceId = 98, dest = InterfaceDestination.MAIN_SCREEN)
        player.message("Interface 98")
        wait(10)
        player.openInterface(interfaceId = 96, dest = InterfaceDestination.MAIN_SCREEN)
        player.message("Interface 96")
        wait(10)
        player.openInterface(interfaceId = 110, dest = InterfaceDestination.MAIN_SCREEN)
        player.message("Interface 110")
        wait(3)
        }

    }



