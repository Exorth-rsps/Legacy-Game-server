package org.alter.plugins.content.area.legacy.dwarven_mine.shops
import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Hendors Awesome Ores and Bars", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    // Wooden Shield
    items[0] = ShopItem(Items.COPPER_ORE, 20, 25, 1, 1, 10)
    items[1] = ShopItem(Items.TIN_ORE, 20, 25, 1, 1, 100)
    items[2] = ShopItem(Items.IRON_ORE, 15, 75, 1, 1, 100)
    items[3] = ShopItem(Items.COAL, 150, 150, 1, 1, 100)
    items[4] = ShopItem(Items.GOLD_ORE, 0, 500, 1, 1, 200)
    items[5] = ShopItem(Items.MITHRIL_ORE, 0, 1200, 1, 1, 200)
    items[6] = ShopItem(Items.ADAMANTITE_ORE, 0, 2200, 1, 1, 200)
    items[7] = ShopItem(Items.RUNITE_ORE, 0, 4200, 1, 1, 200)
    items[8] = ShopItem(Items.BRONZE_BAR, 20, 75, 1, 1, 200)
    items[9] = ShopItem(Items.IRON_BAR, 20, 100, 1, 1, 200)
    items[10] = ShopItem(Items.STEEL_BAR, 20, 500, 1, 1, 200)
}