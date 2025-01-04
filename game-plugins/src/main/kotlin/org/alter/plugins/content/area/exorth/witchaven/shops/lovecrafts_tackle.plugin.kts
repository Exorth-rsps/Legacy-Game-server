package org.alter.plugins.content.area.exorth.witchaven.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Lovecraft's Tackle", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.SMALL_FISHING_NET, 5, 5, 2, 1, 100)
    items[1] = ShopItem(Items.FISHING_ROD, 5, 5, 2, 1, 100)
    items[2] = ShopItem(Items.FLY_FISHING_ROD, 5, 5, 2, 1, 100)
    items[3] = ShopItem(Items.FISHING_BAIT, 150, 3, 1, 1, 100)
    items[4] = ShopItem(Items.BAIT_PACK, 2, 300, 10, 1, 100)
    items[5] = ShopItem(Items.FEATHER, 150, 3, 1, 1, 100)
    items[6] = ShopItem(Items.FEATHER_PACK, 2, 300, 10, 1, 100)
    items[7] = ShopItem(Items.RAW_SHRIMPS, 2, 23, 2, 1, 100)
    items[8] = ShopItem(Items.RAW_SARDINE, 7, 26, 3, 1, 100)
    items[9] = ShopItem(Items.RAW_HERRING, 4, 29, 4, 1, 100)
    items[10] = ShopItem(Items.RAW_ANCHOVIES, 1, 32, 5, 1, 100)
}