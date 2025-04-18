package org.alter.plugins.content.objects.bank_chests

import org.alter.plugins.content.interfaces.bank.openBank

private val CHESTS = with(Objs) { setOf(
        BANK_CHEST_28861,
        BANK_CHEST_19051,
        BANK_CHEST_4483
) }

CHESTS.forEach { booth ->
    on_obj_option(obj = booth, option = "use") {
        player.openBank()
    }
    if (objHasOption(booth, "Collect")) {
        on_obj_option(obj = booth, option = "Collect") {
            open_collect(player)
        }
    }
}

fun open_collect(p: Player) {
    p.setInterfaceUnderlay(color = -1, transparency = -1)
    p.openInterface(interfaceId = 402, dest = InterfaceDestination.MAIN_SCREEN)
}

