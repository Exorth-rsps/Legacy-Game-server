package org.alter.plugins.content.area.legacy.draynor.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Bobs Brilliant Tools", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    // Wooden Shield
    items[0] = ShopItem(Items.BRONZE_AXE, 10, 10, 1, 1, 500)
    items[1] = ShopItem(Items.BRONZE_PICKAXE, 10, 10, 1, 1, 500)
    items[2] = ShopItem(Items.IRON_AXE, 10, 50, 1, 1, 500)
    items[3] = ShopItem(Items.IRON_PICKAXE, 10, 50, 1, 1, 500)
    items[4] = ShopItem(Items.STEEL_AXE, 5, 150, 1, 1, 500)
    items[5] = ShopItem(Items.STEEL_PICKAXE, 5, 150, 1, 1, 500)
    items[6] = ShopItem(Items.BLACK_AXE, 2, 750, 100, 1, 500)
    items[7] = ShopItem(Items.BLACK_PICKAXE, 2, 750, 100, 1, 500)
    items[8] = ShopItem(Items.MITHRIL_AXE, 1, 1500, 500, 1, 500)
    items[9] = ShopItem(Items.MITHRIL_PICKAXE, 1, 1500, 500, 1, 500)
}
