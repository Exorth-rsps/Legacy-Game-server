package org.alter.plugins.content.area.exorth.catherby.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Arhein Store", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_TRADEABLES) {
    items[0] = ShopItem(Items.BUCKET, 10, 2, 0, 1, 100)
    items[1] = ShopItem(Items.EMPTY_BUCKET_PACK, 1, 650, 0, 1, 100)
    items[2] = ShopItem(Items.BRONZE_PICKAXE, 2, 1, 0, 1, 100)
    items[3] = ShopItem(Items.TINDERBOX, 2, 1, 0, 1, 100)
    items[4] = ShopItem(Items.CHISEL, 2, 2, 0,1,100)
    items[5] = ShopItem(Items.HAMMER, 5, 5, 0,1,100)
    items[6] = ShopItem(Items.KNIFE, 2, 7, 0,1,100)
}