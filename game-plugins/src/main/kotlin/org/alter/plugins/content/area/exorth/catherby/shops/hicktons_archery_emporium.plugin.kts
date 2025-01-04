package org.alter.plugins.content.area.exorth.catherby.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Hickton's Archery Emporium", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_TRADEABLES) {
    items[0] = ShopItem(Items.BRONZE_BOLTS, 200, 5, 0, 1, 10)
    items[1] = ShopItem(Items.IRON_BOLTS, 150, 10, 1, 1, 10)
    items[2] = ShopItem(Items.STEEL_BOLTS, 50, 75, 6, 1, 10)
    items[3] = ShopItem(Items.BRONZE_ARROW, 200, 5, 0, 1, 10)
    items[4] = ShopItem(Items.IRON_ARROW, 150, 10, 1,1,10)
    items[5] = ShopItem(Items.STEEL_ARROW, 50, 75, 6,1,10)
    items[6] = ShopItem(Items.BRONZE_ARROWTIPS, 200, 5, 0,1,10)
    items[7] = ShopItem(Items.IRON_ARROWTIPS, 150, 10, 0,1,10)
    items[8] = ShopItem(Items.STEEL_ARROWTIPS, 50, 75, 0,1,10)
    items[9] = ShopItem(Items.SHORTBOW, 4, 50, 10,1,100)
    items[10] = ShopItem(Items.LONGBOW, 2, 80, 10,1,100)
    items[11] = ShopItem(Items.OAK_SHORTBOW, 2, 100, 10,1,200)
    items[12] = ShopItem(Items.OAK_LONGBOW, 1, 160, 10,1,1000)
    items[13] = ShopItem(Items.STUDDED_BODY, 2, 850, 10,1,1000)
    items[14] = ShopItem(Items.STUDDED_CHAPS, 2, 750, 10,1,1000)
    items[15] = ShopItem(Items.COIF, 2, 550, 10,1,1000)
    items[16] = ShopItem(Items.BRONZE_CROSSBOW, 3, 262, 10,1,2000)
    items[17] = ShopItem(Items.IRON_CROSSBOW, 2, 565, 10,1,2000)
    items[18] = ShopItem(Items.STEEL_CROSSBOW, 1, 1296, 10,1,2000)
}