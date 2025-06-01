package org.alter.plugins.content.area.legacy.entrana.shops
import org.alter.game.model.shop.PurchasePolicy
import org.alter.plugins.content.mechanics.shops.CoinCurrency

create_shop("Twiggys Skill Capes", CoinCurrency(), purchasePolicy = PurchasePolicy.BUY_NONE) {
    // Wooden Shield
    items[0] = ShopItem(Items.ATTACK_CAPE, 5, 100000, 1, 1, 100)
    items[1] = ShopItem(Items.STRENGTH_CAPE, 5, 100000, 1, 1, 100)
    items[2] = ShopItem(Items.DEFENCE_CAPE, 5, 100000, 1, 1, 100)
    items[3] = ShopItem(Items.RANGING_CAPE, 5, 100000, 1, 1, 100)
    items[4] = ShopItem(Items.PRAYER_CAPE, 5, 100000, 1, 1, 100)
    items[5] = ShopItem(Items.MAGIC_CAPE, 5, 100000, 1, 1, 100)
    items[6] = ShopItem(Items.RUNECRAFT_CAPE, 5, 100000, 1, 1, 100)
    items[7] = ShopItem(Items.HITPOINTS_CAPE, 5, 100000, 1, 1, 100)
    items[8] = ShopItem(Items.AGILITY_CAPE, 5, 100000, 1, 1, 100)
    items[9] = ShopItem(Items.HERBLORE_CAPE, 5, 100000, 1, 1, 100)
    items[10] = ShopItem(Items.THIEVING_CAPE, 5, 100000, 1, 1, 100)
    items[11] = ShopItem(Items.CRAFTING_CAPE, 5, 100000, 1, 1, 100)
    items[12] = ShopItem(Items.FLETCHING_CAPE, 5, 100000, 1, 1, 100)
    items[13] = ShopItem(Items.SLAYER_CAPE, 5, 100000, 1, 1, 100)
    items[14] = ShopItem(Items.MINING_CAPE, 5, 100000, 1, 1, 100)
    items[15] = ShopItem(Items.SMITHING_CAPE, 5, 100000, 1, 1, 100)
    items[16] = ShopItem(Items.FISHING_CAPE, 5, 100000, 1, 1, 100)
    items[17] = ShopItem(Items.COOKING_CAPE, 5, 100000, 1, 1, 100)
    items[18] = ShopItem(Items.FIREMAKING_CAPE, 5, 100000, 1, 1, 100)
    items[19] = ShopItem(Items.WOODCUTTING_CAPE, 5, 100000, 1, 1, 100)
}