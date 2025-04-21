package org.alter.plugins.content.area.legacy.draynor.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.AnimainfusedbarkCurrency
import org.alter.plugins.content.mechanics.shops.CoinCurrency
import org.alter.plugins.content.mechanics.shops.UnidentifiedMineralsCurrency

create_shop("Rommiks Crafty Supplies", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    // Wooden Shield
    items[0] = ShopItem(Items.CHISEL, 10, 10, 1, 1, 100)
    items[1] = ShopItem(Items.NEEDLE, 10, 10, 1, 1, 100)
    items[2] = ShopItem(Items.THREAD, 100, 10, 1, 1, 10)
}
