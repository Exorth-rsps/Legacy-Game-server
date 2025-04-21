package org.alter.plugins.content.mechanics.starter

import org.alter.game.model.attr.NEW_ACCOUNT_ATTR

on_login {
    if (player.attr[NEW_ACCOUNT_ATTR] ?: return@on_login) {
        with (player.inventory) {
            add(Items.TRAINING_SWORD)
            add(Items.TRAINING_SHIELD)
            add(Items.TRAINING_BOW)
            add(Items.TRAINING_ARROWS, 30)
            add(Items.AIR_RUNE, 30)
            add(Items.MIND_RUNE, 30)
            add(Items.COINS_995, 25)
        }
    }
}
