package org.alter.plugins.content.skills.crafting.tanning

val INTERFACE_ID = 324

var softLeatherPrice = 100
var hardLeatherPrice = 300
var snakeSkinPrice = 2000
var templeTrekkingSnakeSkinPrice = 1500
var greenDhidePrice = 2000
var blueDhidePrice = 2500
var redDhidePrice = 3000
var blackDhidePrice = 3750

fun setPrice(hideType: String, price: Int) {
    when (hideType) {
        "softLeather" -> softLeatherPrice = price
        "hardLeather" -> hardLeatherPrice = price
        "snakeSkin" -> snakeSkinPrice = price
        "templeTrekkingSnakeSkin" -> templeTrekkingSnakeSkinPrice = price
        "greenDhide" -> greenDhidePrice = price
        "blueDhide" -> blueDhidePrice = price
        "redDhide" -> redDhidePrice = price
        "blackDhide" -> blackDhidePrice = price
    }
}


on_interface_open(INTERFACE_ID) {
    // Soft leather
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 100, item = 1739, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 108, text = "Soft leather")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 116, text = "$softLeatherPrice coins")

    // Hard leather
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 101, item = 1739, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 109, text = "Hard leather")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 117, text = "$hardLeatherPrice coins")

    // Snake skin
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 102, item = 6287, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 110, text = "Snakeskin")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 118, text = "$snakeSkinPrice coins")

    // Snake hide
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 103, item = 7801, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 111, text = "Snakeskin")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 119, text = "$templeTrekkingSnakeSkinPrice coins")

    // Green d'hide
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 104, item = 1753, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 112, text = "Green d'hide")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 120, text = "$greenDhidePrice coins")

    // Blue d'hide
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 105, item = 1751, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 113, text = "Blue d'hide")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 121, text = "$blueDhidePrice coins")

    // Red d'hide
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 106, item = 1749, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 114, text = "Red d'hide")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 122, text = "$redDhidePrice coins")

    // Black d'hide
    player.setComponentItem(interfaceId = INTERFACE_ID, component = 107, item = 1747, amountOrZoom = 250)
    player.setComponentText(interfaceId = INTERFACE_ID, component = 115, text = "Black d'hide")
    player.setComponentText(interfaceId = INTERFACE_ID, component = 123, text = "$blackDhidePrice coins")
}


// Soft Leather
on_button(interfaceId = 324, component = 148) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) < softLeatherPrice) {
        player.message("You haven't got enough coins to pay for soft leather.")
    } else if (inventory.getItemCount(Items.COWHIDE) < 1) {
        player.message("You don't have any cowhides to tan.")
    } else {
        inventory.remove(Items.COWHIDE, 1)
        inventory.remove(Items.COINS_995, softLeatherPrice)
        inventory.add(Items.LEATHER)
    }
}

on_button(interfaceId = 324, component = 140) {
    // Make-5
    val inventory = player.inventory
    val amount = 5

    if (inventory.getItemCount(Items.COINS_995) < amount * softLeatherPrice) {
        player.message("You haven't got enough coins to pay for soft leather.")
    } else if (inventory.getItemCount(Items.COWHIDE) < amount) {
        player.message("You don't have enough cowhides to tan.")
    } else {
        val actualAmount = minOf(amount, inventory.getItemCount(Items.COWHIDE), inventory.getItemCount(Items.COINS_995) / softLeatherPrice)
        inventory.remove(Items.COWHIDE, actualAmount)
        inventory.remove(Items.COINS_995, actualAmount * softLeatherPrice)
        inventory.add(Items.LEATHER, actualAmount)
    }
}

// Hard Leather
on_button(interfaceId = 324, component = 149) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) < hardLeatherPrice) {
        player.message("You haven't got enough coins to pay for hard leather.")
    } else if (inventory.getItemCount(Items.COWHIDE) < 1) {
        player.message("You don't have any cowhides to tan.")
    } else {
        inventory.remove(Items.COWHIDE, 1)
        inventory.remove(Items.COINS_995, hardLeatherPrice)
        inventory.add(Items.HARD_LEATHER)
    }
}

on_button(interfaceId = 324, component = 141) {
    // Make-5
    val inventory = player.inventory
    val amount = 5

    if (inventory.getItemCount(Items.COINS_995) < amount * hardLeatherPrice) {
        player.message("You haven't got enough coins to pay for hard leather.")
    } else if (inventory.getItemCount(Items.COWHIDE) < amount) {
        player.message("You don't have enough cowhides to tan.")
    } else {
        val actualAmount = minOf(amount, inventory.getItemCount(Items.COWHIDE), inventory.getItemCount(Items.COINS_995) / hardLeatherPrice)
        inventory.remove(Items.COWHIDE, actualAmount)
        inventory.remove(Items.COINS_995, actualAmount * hardLeatherPrice)
        inventory.add(Items.HARD_LEATHER, actualAmount)
    }
}

// Snake-Hide Norm
on_button(interfaceId = 324, component = 150) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) < snakeSkinPrice) {
        player.message("You haven't got enough coins to pay for snake skin.")
    } else if (inventory.getItemCount(Items.SNAKE_HIDE) < 1) {
        player.message("You don't have any snake hides to tan.")
    } else {
        inventory.remove(Items.SNAKE_HIDE, 1)
        inventory.remove(Items.COINS_995, snakeSkinPrice)
        inventory.add(Items.SNAKESKIN)
    }
}

on_button(interfaceId = 324, component = 142) {
    // Make-5
    val inventory = player.inventory
    val amount = 5

    if (inventory.getItemCount(Items.COINS_995) < amount * snakeSkinPrice) {
        player.message("You haven't got enough coins to pay for snake skin.")
    } else if (inventory.getItemCount(Items.SNAKE_HIDE) < amount) {
        player.message("You don't have enough snake hides to tan.")
    } else {
        val actualAmount = minOf(amount, inventory.getItemCount(Items.SNAKE_HIDE), inventory.getItemCount(Items.COINS_995) / snakeSkinPrice)
        inventory.remove(Items.SNAKE_HIDE, actualAmount)
        inventory.remove(Items.COINS_995, actualAmount * snakeSkinPrice)
        inventory.add(Items.SNAKESKIN, actualAmount)
    }
}

// Snake-Hide Temple Trekking
on_button(interfaceId = 324, component = 151) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) < templeTrekkingSnakeSkinPrice) {
        player.message("You haven't got enough coins to pay for snake skin.")
    } else if (inventory.getItemCount(Items.SNAKE_HIDE_7801) < 1) {
        player.message("You don't have any snake hides to tan.")
    } else {
        inventory.remove(Items.SNAKE_HIDE_7801, 1)
        inventory.remove(Items.COINS_995, templeTrekkingSnakeSkinPrice)
        inventory.add(Items.SNAKESKIN)
    }
}

on_button(interfaceId = 324, component = 143) {
    // Make-5
    val inventory = player.inventory
    val amount = 5

    if (inventory.getItemCount(Items.COINS_995) < amount * templeTrekkingSnakeSkinPrice) {
        player.message("You haven't got enough coins to pay for snake skin.")
    } else if (inventory.getItemCount(Items.SNAKE_HIDE_7801) < amount) {
        player.message("You don't have enough snake hides to tan.")
    } else {
        val actualAmount = minOf(amount, inventory.getItemCount(Items.SNAKE_HIDE_7801), inventory.getItemCount(Items.COINS_995) / templeTrekkingSnakeSkinPrice)
        inventory.remove(Items.SNAKE_HIDE_7801, actualAmount)
        inventory.remove(Items.COINS_995, actualAmount * templeTrekkingSnakeSkinPrice)
        inventory.add(Items.SNAKESKIN, actualAmount)
    }
}

// Green D'Hide
on_button(interfaceId = 324, component = 152) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) < greenDhidePrice) {
        player.message("You haven't got enough coins to pay for green d'hide.")
    } else if (inventory.getItemCount(Items.GREEN_DRAGONHIDE) < 1) {
        player.message("You don't have any green dragonhides to tan.")
    } else {
        inventory.remove(Items.GREEN_DRAGONHIDE, 1)
        inventory.remove(Items.COINS_995, greenDhidePrice)
        inventory.add(Items.GREEN_DRAGON_LEATHER)
    }
}

on_button(interfaceId = 324, component = 144) {
    val inventory = player.inventory
    val amount = 5

    if (inventory.getItemCount(Items.COINS_995) < amount * greenDhidePrice) {
        player.message("You haven't got enough coins to pay for green d'hide.")
    } else if (inventory.getItemCount(Items.GREEN_DRAGONHIDE) < amount) {
        player.message("You don't have enough green dragonhides to tan.")
    } else {
        val actualAmount = minOf(amount, inventory.getItemCount(Items.GREEN_DRAGONHIDE), inventory.getItemCount(Items.COINS_995) / greenDhidePrice)
        inventory.remove(Items.GREEN_DRAGONHIDE, actualAmount)
        inventory.remove(Items.COINS_995, actualAmount * greenDhidePrice)
        inventory.add(Items.GREEN_DRAGON_LEATHER, actualAmount)
    }
}

// Blue D'Hide
on_button(interfaceId = 324, component = 153) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) < blueDhidePrice) {
        player.message("You haven't got enough coins to pay for blue d'hide.")
    } else if (inventory.getItemCount(Items.BLUE_DRAGONHIDE) < 1) {
        player.message("You don't have any blue dragonhides to tan.")
    } else {
        inventory.remove(Items.BLUE_DRAGONHIDE, 1)
        inventory.remove(Items.COINS_995, blueDhidePrice)
        inventory.add(Items.BLUE_DRAGON_LEATHER)
    }
}

on_button(interfaceId = 324, component = 145) {
    val inventory = player.inventory
    val amount = 5

    if (inventory.getItemCount(Items.COINS_995) < amount * blueDhidePrice) {
        player.message("You haven't got enough coins to pay for blue d'hide.")
    } else if (inventory.getItemCount(Items.BLUE_DRAGONHIDE) < amount) {
        player.message("You don't have enough blue dragonhides to tan.")
    } else {
        val actualAmount = minOf(amount, inventory.getItemCount(Items.BLUE_DRAGONHIDE), inventory.getItemCount(Items.COINS_995) / blueDhidePrice)
        inventory.remove(Items.BLUE_DRAGONHIDE, actualAmount)
        inventory.remove(Items.COINS_995, actualAmount * blueDhidePrice)
        inventory.add(Items.BLUE_DRAGON_LEATHER, actualAmount)
    }
}

// Red D'Hide
on_button(interfaceId = 324, component = 154) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) < redDhidePrice) {
        player.message("You haven't got enough coins to pay for red d'hide.")
    } else if (inventory.getItemCount(Items.RED_DRAGONHIDE) < 1) {
        player.message("You don't have any red dragonhides to tan.")
    } else {
        inventory.remove(Items.RED_DRAGONHIDE, 1)
        inventory.remove(Items.COINS_995, redDhidePrice)
        inventory.add(Items.RED_DRAGON_LEATHER)
    }
}

on_button(interfaceId = 324, component = 146) {
    val inventory = player.inventory
    val amount = 5

    if (inventory.getItemCount(Items.COINS_995) < amount * redDhidePrice) {
        player.message("You haven't got enough coins to pay for red d'hide.")
    } else if (inventory.getItemCount(Items.RED_DRAGONHIDE) < amount) {
        player.message("You don't have enough red dragonhides to tan.")
    } else {
        val actualAmount = minOf(amount, inventory.getItemCount(Items.RED_DRAGONHIDE), inventory.getItemCount(Items.COINS_995) / redDhidePrice)
        inventory.remove(Items.RED_DRAGONHIDE, actualAmount)
        inventory.remove(Items.COINS_995, actualAmount * redDhidePrice)
        inventory.add(Items.RED_DRAGON_LEATHER, actualAmount)
    }
}

// Black D'Hide
on_button(interfaceId = 324, component = 155) {
    val inventory = player.inventory
    if (inventory.getItemCount(Items.COINS_995) < blackDhidePrice) {
        player.message("You haven't got enough coins to pay for black d'hide.")
    } else if (inventory.getItemCount(Items.BLACK_DRAGONHIDE) < 1) {
        player.message("You don't have any black dragonhides to tan.")
    } else {
        inventory.remove(Items.BLACK_DRAGONHIDE, 1)
        inventory.remove(Items.COINS_995, blackDhidePrice)
        inventory.add(Items.BLACK_DRAGON_LEATHER)
    }
}

on_button(interfaceId = 324, component = 147) {
    val inventory = player.inventory
    val amount = 5

    if (inventory.getItemCount(Items.COINS_995) < amount * blackDhidePrice) {
        player.message("You haven't got enough coins to pay for black d'hide.")
    } else if (inventory.getItemCount(Items.BLACK_DRAGONHIDE) < amount) {
        player.message("You don't have enough black dragonhides to tan.")
    } else {
        val actualAmount = minOf(amount, inventory.getItemCount(Items.BLACK_DRAGONHIDE), inventory.getItemCount(Items.COINS_995) / blackDhidePrice)
        inventory.remove(Items.BLACK_DRAGONHIDE, actualAmount)
        inventory.remove(Items.COINS_995, actualAmount * blackDhidePrice)
        inventory.add(Items.BLACK_DRAGON_LEATHER, actualAmount)
    }
}
