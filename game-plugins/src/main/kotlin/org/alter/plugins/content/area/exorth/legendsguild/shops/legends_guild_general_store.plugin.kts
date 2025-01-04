package org.alter.plugins.content.area.exorth.legendsguild.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Legends Guild General Store", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_TRADEABLES) {
    items[0] = ShopItem(Items.TINDERBOX, 10, 3, 1)
    items[1] = ShopItem(Items.HAMMER, 7, 3, 1)
    items[2] = ShopItem(Items.KNIFE, 6, 4, 1)
    items[3] = ShopItem(Items.CHISEL, 3, 5, 1)
    items[4] = ShopItem(Items.SHEARS, 2, 9, 1)
}