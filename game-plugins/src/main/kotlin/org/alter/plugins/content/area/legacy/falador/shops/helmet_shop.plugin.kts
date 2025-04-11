package org.alter.plugins.content.area.legacy.falador.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Helmet Shop", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.BRONZE_MED_HELM, 5, 36, 18, 1, 100)
    items[1] = ShopItem(Items.IRON_MED_HELM, 3, 132, 66, 1, 200)
    items[2] = ShopItem(Items.STEEL_MED_HELM, 2, 468, 234, 1, 300)
    items[3] = ShopItem(Items.BLACK_MED_HELM, 1, 900, 450, 1, 350)
    items[4] = ShopItem(Items.MITHRIL_MED_HELM, 1, 1300, 650, 1, 400)
    items[5] = ShopItem(Items.BRONZE_FULL_HELM, 4, 72, 36, 1, 100)
    items[6] = ShopItem(Items.IRON_FULL_HELM, 3, 264, 132, 1, 200)
    items[7] = ShopItem(Items.STEEL_FULL_HELM, 2, 936, 468, 1, 300)
    items[8] = ShopItem(Items.BLACK_FULL_HELM, 10, 1500, 750, 1, 400)
}
