package org.alter.plugins.content.area.legacy.draynor.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.AnimainfusedbarkCurrency
import org.alter.plugins.content.mechanics.shops.CoinCurrency
import org.alter.plugins.content.mechanics.shops.UnidentifiedMineralsCurrency

create_shop("Forester Store", AnimainfusedbarkCurrency(), purchasePolicy = PurchasePolicy.BUY_NONE) {
    // Wooden Shield
    items[0] = ShopItem(Items.LUMBERJACK_BOOTS, 1, 1200, 1200, 1, 500)
    items[1] = ShopItem(Items.LUMBERJACK_HAT, 1, 1200, 1200, 1, 500)
    items[2] = ShopItem(Items.LUMBERJACK_LEGS, 1, 1200, 1200, 1, 500)
    items[3] = ShopItem(Items.LUMBERJACK_TOP, 1, 2500, 2500, 1, 500)
    items[4] = ShopItem(Items.FORESTRY_BOOTS, 1, 2500, 1200, 1, 800)
    items[5] = ShopItem(Items.FORESTRY_HAT, 1, 2500, 1200, 1, 800)
    items[6] = ShopItem(Items.FORESTRY_LEGS, 1, 2500, 1200, 1, 800)
    items[7] = ShopItem(Items.FORESTRY_TOP, 1, 2500, 1200, 1, 800)
    items[8] = ShopItem(Items.DRAGON_AXE, 1, 5000, 1200, 1, 800)
}
