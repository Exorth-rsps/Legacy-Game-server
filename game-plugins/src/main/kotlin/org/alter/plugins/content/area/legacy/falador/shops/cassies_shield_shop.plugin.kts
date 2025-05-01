package org.alter.plugins.content.area.legacy.falador.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Cassie's Shield Shop", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    // Wooden Shield
    items[0] = ShopItem(Items.WOODEN_SHIELD, 5, 10, 5, 1, 50)
    items[1] = ShopItem(Items.BRONZE_SQ_SHIELD, 4, 48, 24, 1, 100)
    items[2] = ShopItem(Items.IRON_SQ_SHIELD, 3, 172, 86, 1, 200)
    items[3] = ShopItem(Items.STEEL_SQ_SHIELD, 2, 600, 300, 1, 300)
    items[4] = ShopItem(Items.BLACK_SQ_SHIELD, 1, 1200, 600, 1, 400)
    items[5] = ShopItem(Items.BRONZE_KITESHIELD, 3, 64, 32, 1, 100)
    items[6] = ShopItem(Items.IRON_KITESHIELD, 2, 224, 112, 1, 200)
    items[7] = ShopItem(Items.STEEL_KITESHIELD, 1, 1200, 400, 1, 300)
    items[8] = ShopItem(Items.ANTIDRAGON_SHIELD, 5, 555, 100, 1, 300)
}
