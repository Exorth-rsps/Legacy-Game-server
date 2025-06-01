package org.alter.plugins.content.skills.runecrafting

private val enterOption = "Enter"

// Specified ruins IDs: 32489 wordt slechts voor talisman gebruik, 32490 heeft ook de 'Enter' optie
val talismanOnlyRuin = Objs.MYSTERIOUS_RUINS_32489
val optionRuin = Objs.MYSTERIOUS_RUINS_32490

// Handle het gebruik van een talisman op talismanOnlyRuin om naar respective altar te teleporteren
talismanOnlyRuin.let { ruinId ->
    Altar.values.forEach { altar ->
        altar.talisman?.let { talisman ->
            on_item_on_obj(obj = ruinId, item = talisman, lineOfSightDistance = 5) {
                altar.entrance?.let { player.moveTo(it) }
            }
        }
    }
}

// Handle het gebruik van een talisman op optionRuin om naar respective altar te teleporteren
optionRuin.let { ruinId ->
    Altar.values.forEach { altar ->
        altar.talisman?.let { talisman ->
            on_item_on_obj(obj = ruinId, item = talisman, lineOfSightDistance = 5) {
                altar.entrance?.let { player.moveTo(it) }
            }
        }
    }

    // Handle de 'Enter'-option op optionRuin: bepaal op basis van actieve varbit welk altar
    on_obj_option(obj = ruinId, option = enterOption) {
        val activeAltar = Altar.values.firstOrNull { player.getVarbit(it.varbit) == 1 }
        activeAltar?.entrance?.let { player.moveTo(it) }
    }
}

// Handle equip/unequip van tiaras om varbit aan/uit te zetten
Altar.values.forEach { altar ->
    altar.tiara?.let {
        on_item_equip(item = it) {
            player.setVarbit(altar.varbit, 1)
        }
        on_item_unequip(item = it) {
            player.setVarbit(altar.varbit, 0)
        }
    }
}

// Handle het craften van runes bij elk altar
Altar.values.forEach { altar ->
    on_obj_option(obj = altar.altar, option = altar.option) {
        player.queue {
            RunecraftAction.craftRune(this, altar.rune)
        }
    }
}

// Handle de exit portals: exits teleporteren naar het exit-tile in Alter.kt
Altar.values.forEach { altar ->
    if (altar.exitPortal != null && altar.exit != null) {
        on_obj_option(obj = altar.exitPortal, option = "use") {
            player.moveTo(altar.exit)
        }
    }
}

// Handle de 'locate'-optie op een talisman (toont richting naar de optionRuin)
Altar.values.forEach { altar ->
    altar.talisman?.let {
        on_item_option(item = it, option = "locate") {
            val tile = altar.exit!!  // richting bepalen aan de hand van elke altar's exit
            val pos = player.tile
            val direction: String = when {
                pos.z > tile.z && pos.x - 1 > tile.x -> "south-west"
                pos.x < tile.x && pos.z > tile.z -> "south-east"
                pos.x > tile.x + 1 && pos.z < tile.z -> "north-west"
                pos.x < tile.x && pos.z < tile.z -> "north-east"
                pos.z < tile.z -> "north"
                pos.z > tile.z -> "south"
                pos.x < tile.x + 1 -> "east"
                pos.x > tile.x + 1 -> "west"
                else -> "unknown"
            }
            player.message("The talisman pulls towards the $direction.")
        }
    }
}