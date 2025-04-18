package org.alter.plugins.content.area.legacy.dwarven_mine.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency
import org.alter.plugins.content.mechanics.shops.UnidentifiedMineralsCurrency

create_shop("Mining Guild Mineral Exchange", UnidentifiedMineralsCurrency(), purchasePolicy = PurchasePolicy.BUY_NONE) {
    // Wooden Shield
    items[0] = ShopItem(Items.MINING_GLOVES, 5, 400, 400, 1, 500)
    items[1] = ShopItem(Items.SUPERIOR_MINING_GLOVES, 3, 800, 800, 1, 500)
    items[2] = ShopItem(Items.EXPERT_MINING_GLOVES, 1, 1200, 1200, 1, 500)
    items[3] = ShopItem(Items.DRAGON_PICKAXE, 1, 2500, 2500, 1, 500)
    items[4] = ShopItem(Items.PROSPECTOR_HELMET, 1, 1200, 1200, 1, 500)
    items[5] = ShopItem(Items.PROSPECTOR_JACKET, 1, 1200, 1200, 1, 500)
    items[6] = ShopItem(Items.PROSPECTOR_LEGS, 1, 1200, 1200, 1, 500)
    items[7] = ShopItem(Items.PROSPECTOR_BOOTS, 1, 1200, 1200, 1, 500)
    items[8] = ShopItem(Items.GOLDEN_PROSPECTOR_HELMET, 1, 2500, 1200, 1, 800)
    items[9] = ShopItem(Items.GOLDEN_PROSPECTOR_JACKET, 1, 2500, 1200, 1, 800)
    items[10] = ShopItem(Items.GOLDEN_PROSPECTOR_LEGS, 1, 2500, 1200, 1, 800)
    items[11] = ShopItem(Items.GOLDEN_PROSPECTOR_BOOTS, 1, 2500, 1200, 1, 800)
}
