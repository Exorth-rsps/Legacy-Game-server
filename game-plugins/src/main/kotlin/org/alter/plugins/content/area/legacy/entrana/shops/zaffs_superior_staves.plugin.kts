package org.alter.plugins.content.area.legacy.entrana.shops
import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Zaffs Superior Staves", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    // Wooden Shield
    items[0] = ShopItem(Items.STAFF, 10, 7000, 3850, 1, 100)
    items[1] = ShopItem(Items.MAGIC_STAFF, 10, 14000, 7700, 1, 200)
    items[2] = ShopItem(Items.STAFF_OF_AIR, 2, 28000, 14000, 1, 1000)
    items[3] = ShopItem(Items.STAFF_OF_WATER, 2, 28000, 14000, 1, 1000)
    items[4] = ShopItem(Items.STAFF_OF_EARTH, 2, 28000, 14000, 1, 1000)
    items[5] = ShopItem(Items.STAFF_OF_FIRE, 2, 28000, 14000, 1, 1000)
}