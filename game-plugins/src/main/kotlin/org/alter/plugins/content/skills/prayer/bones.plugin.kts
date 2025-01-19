package org.alter.plugins.content.skills.prayer



Bones.values.forEach { bones ->
    on_item_option(item = bones.id, option = "bury"){
        if (!Bury.canBury(player, bones)) {
            return@on_item_option
        }
        val inventorySlot = player.getInteractingItemSlot()
        if (player.inventory.remove(item = bones.id, beginSlot = inventorySlot).hasSucceeded()) {
            Bury.Bury(player, bones)
        }
    }
    on_item_on_obj(obj = Objs.CHAOS_ALTAR_412, item = bones.id) {
        if (!Offer.canOffer(player, bones)) {
            return@on_item_on_obj
        }
        if (player.inventory.contains(bones.id)) {
            // 0(gilded), 1(ecto), 2(chaos), 3(normal)
            Offer.OfferBones(player, bones, 2)
        }
    }
    on_item_on_obj(obj = Objs.ALTAR_409, item = bones.id) {
        if (!Offer.canOffer(player, bones)) {
            return@on_item_on_obj
        }
        if (player.inventory.contains(bones.id)) {
            // 0(gilded), 1(ecto), 2(chaos), 3(normal)
            Offer.OfferBones(player, bones, 3)
        }
    }
}