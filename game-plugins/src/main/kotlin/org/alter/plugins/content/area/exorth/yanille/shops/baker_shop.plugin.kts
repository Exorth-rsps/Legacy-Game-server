package org.alter.plugins.content.area.exorth.yanille.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Golden Crust Bakery", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.BREAD, 15, 12, 1, 1, 100)
    items[1] = ShopItem(Items.SLICE_OF_CAKE, 5, 50, 1, 1, 100)
    items[2] = ShopItem(Items.CHOCOLATE_CAKE, 1, 300, 1, 1, 100)
}

create_shop("Blissful Bakery", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.BREAD, 15, 12, 1, 1, 100)
    items[1] = ShopItem(Items.CAKE, 2, 150, 1, 1, 100)
    items[2] = ShopItem(Items.CHOCOLATE_SLICE, 3, 75, 1, 1, 100)
}