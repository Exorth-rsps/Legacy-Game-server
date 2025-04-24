package org.alter.plugins.content.area.legacy.fishing_guild.shops

import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Harry's Fishing Shop", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.SMALL_FISHING_NET, 5, 15, 0, 1, 100)
    items[1] = ShopItem(Items.FISHING_ROD, 5, 25, 1, 1, 100)
    items[2] = ShopItem(Items.LOBSTER_POT, 2, 75, 1, 1, 400)
    items[3] = ShopItem(Items.FISHING_BAIT, 800, 3, 0, 1, 5)
    //items[4] = ShopItem(Items.BAIT_PACK, 5, 500, 1,1,400)
    items[5] = ShopItem(Items.HARPOON, 1, 300, 1,1,400)
    items[6] = ShopItem(Items.RAW_SHRIMPS, 0, 50, 5,1,1000)
    items[7] = ShopItem(Items.RAW_SARDINE, 0, 100, 10,1,1000)
    items[8] = ShopItem(Items.RAW_HERRING, 0, 100, 10,1,1000)
    items[9] = ShopItem(Items.RAW_MACKEREL, 0, 150, 15,1,1000)
    items[10] = ShopItem(Items.RAW_COD, 0, 100, 10,1,1000)
    items[11] = ShopItem(Items.RAW_ANCHOVIES, 0, 150, 15,1,1000)
    items[12] = ShopItem(Items.RAW_TUNA, 0, 400, 40,1,1000)
    items[13] = ShopItem(Items.RAW_LOBSTER, 0, 700, 70,1,1000)
    items[14] = ShopItem(Items.RAW_BASS, 0, 400, 40,1,1000)
    items[15] = ShopItem(Items.RAW_SWORDFISH, 0, 800, 80,1,1000)
    items[16] = ShopItem(Items.RAW_SHARK, 0, 1700, 170,1,1000)
    items[17] = ShopItem(Items.RAW_SEA_TURTLE, 0, 2200, 220,1,2000)
    items[18] = ShopItem(Items.RAW_MANTA_RAY, 0, 3100, 310,1,2000)
}