package org.alter.plugins.content.area.exorth.yanille.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Magic Guild Store", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.AIR_RUNE, 200, 5, 0, 1, 10)
    items[1] = ShopItem(Items.WATER_RUNE, 150, 6, 1, 1, 10)
    items[2] = ShopItem(Items.EARTH_RUNE, 50, 7, 6, 1, 10)
    items[3] = ShopItem(Items.MIND_RUNE, 200, 5, 0, 1, 10)
    items[4] = ShopItem(Items.BODY_RUNE, 150, 10, 1,1,10)
    items[5] = ShopItem(Items.CHAOS_RUNE, 50, 75, 6,1,10)
    items[6] = ShopItem(Items.STAFF, 5, 250, 0,1,100)
    items[7] = ShopItem(Items.STAFF_OF_AIR, 2, 5000, 0,1,1000)
    items[8] = ShopItem(Items.STAFF_OF_WATER, 2, 6000, 0,1,1000)
    items[9] = ShopItem(Items.STAFF_OF_EARTH, 2, 7500, 10,1,1000)
    items[10] = ShopItem(Items.BLUE_WIZARD_HAT, 2, 400, 10,1,1000)
    items[11] = ShopItem(Items.WIZARD_HAT, 2, 400, 10,1,1000)
}