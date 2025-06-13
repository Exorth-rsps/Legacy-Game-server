package org.alter.plugins.content.interfaces.social

import org.alter.api.InterfaceDestination
import org.alter.api.cfg.Varbit
import org.alter.api.ext.openInterface
import org.alter.api.ext.setVarbit

on_login {
    player.openInterface(dest = InterfaceDestination.SOCIAL)
    player.setVarbit(Varbit.FRIEND_FACE_ID_VARBIT, 0)
}
