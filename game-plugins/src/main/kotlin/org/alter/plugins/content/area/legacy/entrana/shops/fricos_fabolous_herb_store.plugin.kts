package org.alter.plugins.content.area.legacy.entrana.shops
import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Frincos Fabulous Herb Store", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    // Wooden Shield
    items[0] = ShopItem(Items.VIAL, 50, 2, 1, 1, 100)
    items[1] = ShopItem(Items.VIAL_NOTED, 50, 2, 1, 1, 100)
    items[2] = ShopItem(Items.VIAL_OF_WATER, 45, 5, 1, 1, 100)
    items[3] = ShopItem(Items.VIAL_OF_WATER_NOTED, 40, 10, 1, 1, 100)
    items[4] = ShopItem(Items.EYE_OF_NEWT, 50, 3, 1, 1, 100)
    items[5] = ShopItem(Items.EYE_OF_NEWT_NOTED, 50, 6, 1, 1, 100)
}