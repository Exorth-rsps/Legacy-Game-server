package org.alter.plugins.content.area.legacy.falador.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Frenita's Cooked Food Shop", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.COOKED_MEAT, 15, 5, 1, 1, 100)
    items[1] = ShopItem(Items.COOKED_CHICKEN, 15, 5, 1, 1, 100)
    items[2] = ShopItem(Items.BREAD, 10, 7, 5, 1, 100)
    items[3] = ShopItem(Items.BAKED_POTATO, 5, 12, 1, 1, 100)
    items[4] = ShopItem(Items.PLAIN_PIZZA, 2, 53, 1,1,100)
    items[5] = ShopItem(Items.SHRIMPS, 23, 7, 1,1,100)
    items[6] = ShopItem(Items.HERRING, 12, 27, 1,1,100)
}