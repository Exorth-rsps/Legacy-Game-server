package org.alter.plugins.content.area.legacy.falador.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Gaius' Sword Shop", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.BRONZE_DAGGER, 10, 20, 10, 1, 100)
    items[1] = ShopItem(Items.IRON_DAGGER, 5, 70, 35, 1, 150)
    items[2] = ShopItem(Items.STEEL_DAGGER, 3, 250, 125, 1, 200)
    items[3] = ShopItem(Items.BLACK_DAGGER, 2, 500, 250, 1, 250)
    items[4] = ShopItem(Items.MITHRIL_DAGGER, 1, 1000, 500, 1, 300)
    items[5] = ShopItem(Items.BRONZE_SWORD, 8, 32, 16, 1, 100)
    items[6] = ShopItem(Items.IRON_SWORD, 6, 112, 56, 1, 150)
    items[7] = ShopItem(Items.STEEL_SWORD, 4, 400, 200, 1, 200)
    items[8] = ShopItem(Items.BLACK_SWORD, 2, 800, 400, 1, 250)
    items[9] = ShopItem(Items.BRONZE_LONGSWORD, 6, 48, 24, 1, 100)
    items[10] = ShopItem(Items.IRON_LONGSWORD, 3, 168, 84, 1, 150)
    items[11] = ShopItem(Items.STEEL_LONGSWORD, 1, 600, 300, 1, 200)
    items[12] = ShopItem(Items.BRONZE_SCIMITAR, 6, 56, 28, 1, 100)
    items[13] = ShopItem(Items.IRON_SCIMITAR, 2, 210, 105, 1, 150)
    items[14] = ShopItem(Items.STEEL_SCIMITAR, 1, 750, 375, 1, 200)
    items[15] = ShopItem(Items.BRONZE_2H_SWORD, 3, 64, 32, 1, 100)
    items[16] = ShopItem(Items.IRON_2H_SWORD, 2, 224, 112, 1, 150)
    items[17] = ShopItem(Items.STEEL_2H_SWORD, 1, 800, 400, 1, 200)
}
