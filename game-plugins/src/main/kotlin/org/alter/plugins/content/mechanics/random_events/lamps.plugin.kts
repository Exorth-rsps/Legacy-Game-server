package org.alter.plugins.content.mechanics.random_events

import org.alter.api.cfg.Items
import org.alter.plugins.content.interfaces.xpreward.XpReward

on_item_option(item = Items.LAMP, option = "Rub") {
    XpReward.open(player, Items.LAMP)
}

on_item_option(item = Items.BOOK_OF_KNOWLEDGE, option = "Read") {
    XpReward.open(player, Items.BOOK_OF_KNOWLEDGE)
}
