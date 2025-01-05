package org.alter.plugins.content.area.varrock.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Horvik's Armour Shop", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.BRONZE_CHAINBODY, 10, 60, 30, 1, 100)
    items[1] = ShopItem(Items.IRON_CHAINBODY, 10, 210, 105, 1, 200)
    items[2] = ShopItem(Items.STEEL_CHAINBODY, 10, 750, 375, 1, 300)
    items[3] = ShopItem(Items.BRONZE_PLATEBODY, 10, 160, 80, 1, 100)
    items[4] = ShopItem(Items.IRON_PLATEBODY, 10, 560, 280, 1, 200)
    items[5] = ShopItem(Items.STEEL_PLATEBODY, 10, 2000, 1000, 1, 300)
}
