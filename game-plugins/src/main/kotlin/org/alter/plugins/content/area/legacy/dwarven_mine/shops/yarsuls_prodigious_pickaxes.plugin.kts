package org.alter.plugins.content.area.legacy.dwarven_mine.shops
import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Yarsuls Prodigious Pickaxes", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    // Wooden Shield
    items[0] = ShopItem(Items.BRONZE_PICKAXE, 20, 10, 5, 1, 50)
    items[1] = ShopItem(Items.IRON_PICKAXE, 10, 25, 10, 1, 100)
    items[2] = ShopItem(Items.STEEL_PICKAXE, 5, 150, 25, 1, 200)
    items[3] = ShopItem(Items.BLACK_PICKAXE, 2, 600, 300, 1, 300)
    items[4] = ShopItem(Items.MITHRIL_PICKAXE, 2, 1200, 600, 1, 400)
    items[5] = ShopItem(Items.ADAMANT_PICKAXE, 1, 12400, 1200, 1, 500)
    items[6] = ShopItem(Items.RUNE_PICKAXE, 1, 24000, 12000, 1, 600)
}