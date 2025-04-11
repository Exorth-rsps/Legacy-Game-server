package org.alter.plugins.content.area.legacy.falador.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Louie's Armoured Legs", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.BRONZE_PLATELEGS, 10, 80, 40, 1, 100)
    items[1] = ShopItem(Items.IRON_PLATELEGS, 5, 300, 150, 1, 200)
    items[2] = ShopItem(Items.STEEL_PLATELEGS, 2, 1125, 562, 1, 300)
    items[3] = ShopItem(Items.BRONZE_PLATESKIRT, 10, 80, 40, 1, 100)
    items[4] = ShopItem(Items.IRON_PLATESKIRT, 5, 300, 150, 1, 200)
    items[5] = ShopItem(Items.STEEL_PLATESKIRT, 2, 1125, 562, 1, 300)
}
