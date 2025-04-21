package org.alter.plugins.content.skills.herblore.pots

import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.fs.def.ItemDef
import org.alter.game.model.attr.INTERACTING_ITEM_SLOT
import org.alter.game.model.attr.OTHER_ITEM_SLOT_ATTR
import org.alter.game.model.entity.Player
import org.alter.api.Skills
import org.alter.api.cfg.Items

import org.alter.api.ext.*

// Uitgebreide Pot-klasse die meerdere secondary ingrediënten ondersteunt
class Pot(
    val unfinished: Int,
    val secondaries: IntArray,
    val finished: IntArray,
    private val minLevel: Int,
    private val xpAwarded: Double
) {
    fun make(player: Player) {
        // level-check
        if (player.getSkills().getCurrentLevel(Skills.HERBLORE) < minLevel) {
            player.message("You need level $minLevel Herblore to make this potion.")
            return
        }
        // controleer of alle ingrediënten aanwezig zijn
        if (!player.inventory.contains(unfinished) || !secondaries.all { player.inventory.contains(it) }) {
            player.message("You are missing ingredients to make this potion.")
            return
        }
        // verwijder unfinished + alle secondaries
        player.inventory.remove(unfinished)
        secondaries.forEach { id ->
            player.inventory.remove(id)
        }
        // voeg de 4-dose potion toe
        player.inventory.add(finished.last())
        // animatie, geluid en XP
        player.animate(id = 363, delay = 0)
        player.playSound(2608, 1, 0)
        player.addXp(Skills.HERBLORE, xpAwarded)
        player.message("You have made a ${player.world.definitions.get(ItemDef::class.java, finished.last()).name}.")
    }

    fun charge(player: Player) {
        val srcSlot = player.attr[INTERACTING_ITEM_SLOT]!!
        val dstSlot = player.attr[OTHER_ITEM_SLOT_ATTR]!!
        val chargeCount = finished.indexOf(player.inventory[dstSlot]!!.id) + 1
        if (chargeCount > 3) {
            player.nothingMessage()
        } else if (chargeCount > 0) {
            consume(player)
            if (player.inventory.remove(finished[chargeCount - 1]).hasSucceeded())
                player.inventory.add(finished[chargeCount + 1])
        } else {
            logger.error("invalid chargeCount for pot at ${'$'}{player.inventory[dstSlot]} with charges = $chargeCount")
        }
    }

    fun consume(player: Player) {
        val chargeCount = finished.indexOf(player.getInteractingItem().id) + 1
        // eerste dosis
        if (chargeCount == 1 && player.inventory.remove(finished[0]).hasSucceeded()) {
            player.message("You drink some of your ${'$'}{player.world.definitions.get(ItemDef::class.java, finished[0]).name}.")
            player.animate(829)
            player.inventory.add(Items.VIAL)
            player.message("You have finished your potion.")
        }
        // latere doses
        else if (chargeCount > 1 && player.inventory.remove(finished[chargeCount - 1]).hasSucceeded()) {
            player.message("You drink some of your ${'$'}{player.world.definitions.get(ItemDef::class.java, finished[chargeCount - 1]).name}")
            player.animate(829)
            // zet pot terug met één dosis minder
            player.inventory.add(finished[chargeCount - 2])
            player.message("You have ${'$'}{chargeCount - 1} doses of potion left.")
        } else {
            logger.error("invalid pot registration for ${'$'}{player.getInteractingItem().id}")
        }
    }

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}

enum class Pots(val pot: Pot) {
    ATTACK(Pot(
        unfinished = Items.GUAM_POTION_UNF,
        secondaries = intArrayOf(Items.EYE_OF_NEWT),
        finished = intArrayOf(
            Items.ATTACK_POTION1,
            Items.ATTACK_POTION2,
            Items.ATTACK_POTION3,
            Items.ATTACK_POTION4
        ),
        minLevel = 3,
        xpAwarded = 25.0
    )),
    ANTI_POISON(Pot(
        unfinished = Items.MARRENTILL_POTION_UNF,
        secondaries = intArrayOf(Items.EYE_OF_NEWT),
        finished = intArrayOf(
            Items.ANTIPOISON1,
            Items.ANTIPOISON2,
            Items.ANTIPOISON3,
            Items.ANTIPOISON4
        ),
        minLevel = 5,
        xpAwarded = 37.5
    )),
    STRENGTH(Pot(
        unfinished = Items.TARROMIN_POTION_UNF,
        secondaries = intArrayOf(Items.LIMPWURT_ROOT),
        finished = intArrayOf(
            Items.STRENGTH_POTION1,
            Items.STRENGTH_POTION2,
            Items.STRENGTH_POTION3,
            Items.STRENGTH_POTION4
        ),
        minLevel = 14,
        xpAwarded = 50.0
    )),
    DEFENCE(Pot(
        unfinished = Items.RANARR_POTION_UNF,
        secondaries = intArrayOf(Items.WHITE_BERRIES),
        finished = intArrayOf(
            Items.DEFENCE_POTION1,
            Items.DEFENCE_POTION2,
            Items.DEFENCE_POTION3,
            Items.DEFENCE_POTION4
        ),
        minLevel = 25,
        xpAwarded = 75.0
    )),
    PRAYER(Pot(
        unfinished = Items.RANARR_POTION_UNF,
        secondaries = intArrayOf(Items.SNAPE_GRASS),
        finished = intArrayOf(
            Items.PRAYER_POTION1,
            Items.PRAYER_POTION2,
            Items.PRAYER_POTION3,
            Items.PRAYER_POTION4
        ),
        minLevel = 38,
        xpAwarded = 87.5
    )),
    SUPER_ATTACK(Pot(
        unfinished = Items.IRIT_POTION_UNF,
        secondaries = intArrayOf(Items.EYE_OF_NEWT),
        finished = intArrayOf(
            Items.SUPER_ATTACK1,
            Items.SUPER_ATTACK2,
            Items.SUPER_ATTACK3,
            Items.SUPER_ATTACK4
        ),
        minLevel = 46,
        xpAwarded = 100.0
    )),
    SUPER_STRENGTH(Pot(
        unfinished = Items.KWUARM_POTION_UNF,
        secondaries = intArrayOf(Items.LIMPWURT_ROOT),
        finished = intArrayOf(
            Items.SUPER_STRENGTH1,
            Items.SUPER_STRENGTH2,
            Items.SUPER_STRENGTH3,
            Items.SUPER_STRENGTH4
        ),
        minLevel = 55,
        xpAwarded = 125.0
    )),
    SUPER_RESTORE(Pot(
        unfinished = Items.SNAPDRAGON_POTION_UNF,
        secondaries = intArrayOf(Items.RED_SPIDERS_EGGS),
        finished = intArrayOf(
            Items.SUPER_RESTORE1,
            Items.SUPER_RESTORE2,
            Items.SUPER_RESTORE3,
            Items.SUPER_RESTORE4
        ),
        minLevel = 63,
        xpAwarded = 142.5
    )),
    SUPER_DEFENCE(Pot(
        unfinished = Items.CADANTINE_POTION_UNF,
        secondaries = intArrayOf(Items.WHITE_BERRIES),
        finished = intArrayOf(
            Items.SUPER_DEFENCE1,
            Items.SUPER_DEFENCE2,
            Items.SUPER_DEFENCE3,
            Items.SUPER_DEFENCE4
        ),
        minLevel = 66,
        xpAwarded = 150.0
    )),
    RANGING(Pot(
        unfinished = Items.DWARF_WEED_POTION_UNF,
        secondaries = intArrayOf(Items.WINE_OF_ZAMORAK),
        finished = intArrayOf(
            Items.RANGING_POTION1,
            Items.RANGING_POTION2,
            Items.RANGING_POTION3,
            Items.RANGING_POTION4
        ),
        minLevel = 72,
        xpAwarded = 162.5
    )),
    ANTIFIRE(Pot(
        unfinished = Items.TORSTOL_POTION_UNF,
        secondaries = intArrayOf(Items.WILLOW_ROOTS),
        finished = intArrayOf(
            Items.ANTIFIRE_POTION1,
            Items.ANTIFIRE_POTION2,
            Items.ANTIFIRE_POTION3,
            Items.ANTIFIRE_POTION4
        ),
        minLevel = 79,
        xpAwarded = 157.5
    )),
    ANCIENT_BREW(Pot(
        unfinished = Items.DWARF_WEED_POTION_UNF,
        secondaries = intArrayOf(Items.NIHIL_DUST),
        finished = intArrayOf(
            Items.ANCIENT_BREW1,
            Items.ANCIENT_BREW2,
            Items.ANCIENT_BREW3,
            Items.ANCIENT_BREW4
        ),
        minLevel = 85,
        xpAwarded = 190.0
    )),
    ANTIDOTE_PLUS(Pot(
        unfinished = Items.ANTIDOTE_UNF,
        secondaries = intArrayOf(Items.YEW_ROOTS),
        finished = intArrayOf(
            Items.ANTIDOTE1,
            Items.ANTIDOTE2,
            Items.ANTIDOTE3,
            Items.ANTIDOTE4
        ),
        minLevel = 68,
        xpAwarded = 155.0
    )),
    SUPER_ANTI_POISON(Pot(
        unfinished = Items.ANTIDOTE_UNF_5951,
        secondaries = intArrayOf(Items.MAGIC_ROOTS),
        finished = intArrayOf(
            Items.ANTIDOTE1_5958,
            Items.ANTIDOTE2_5956,
            Items.ANTIDOTE3_5954,
            Items.ANTIDOTE4_5952
        ),
        minLevel = 79,
        xpAwarded = 177.5
    )),
    BATTLE_POTION(Pot(
        unfinished = Items.HARRALANDER_POTION_UNF,
        secondaries = intArrayOf(Items.GOAT_HORN_DUST),
        finished = intArrayOf(
            Items.COMBAT_POTION1,
            Items.COMBAT_POTION2,
            Items.COMBAT_POTION3,
            Items.COMBAT_POTION4
        ),
        minLevel = 36,
        xpAwarded = 84.0
    )),
    BASTION(Pot(
        unfinished = Items.CADANTINE_BLOOD_POTION_UNF,
        secondaries = intArrayOf(Items.WINE_OF_ZAMORAK),
        finished = intArrayOf(
            Items.BASTION_POTION1,
            Items.BASTION_POTION2,
            Items.BASTION_POTION3,
            Items.BASTION_POTION4
        ),
        minLevel = 80,
        xpAwarded = 155.0
    )),
    BATTLEMAGE(Pot(
        unfinished = Items.CADANTINE_BLOOD_POTION_UNF,
        secondaries = intArrayOf(Items.POTATO_CACTUS),
        finished = intArrayOf(
            Items.BATTLEMAGE_POTION1,
            Items.BATTLEMAGE_POTION2,
            Items.BATTLEMAGE_POTION3,
            Items.BATTLEMAGE_POTION4
        ),
        minLevel = 88,
        xpAwarded = 150.0
    )),
    FORGOTTEN_BREW(Pot(
        unfinished = Items.ANCIENT_BREW4,
        secondaries = intArrayOf(Items.ANCIENT_ESSENCE),
        finished = intArrayOf(
            Items.FORGOTTEN_BREW1,
            Items.FORGOTTEN_BREW2,
            Items.FORGOTTEN_BREW3,
            Items.FORGOTTEN_BREW4
        ),
        minLevel = 91,
        xpAwarded = 145.0
    )),
    MAGIC_ESSENCE(Pot(
        unfinished = Items.MAGIC_ESSENCE_UNF,
        secondaries = intArrayOf(Items.GORAK_CLAW_POWDER),
        finished = intArrayOf(
            Items.MAGIC_ESSENCE1,
            Items.MAGIC_ESSENCE2,
            Items.MAGIC_ESSENCE3,
            Items.MAGIC_ESSENCE4
        ),
        minLevel = 57,
        xpAwarded = 130.0
    )),
    MAGIC(Pot(
        unfinished = Items.LANTADYME_POTION_UNF,
        secondaries = intArrayOf(Items.POTATO_CACTUS),
        finished = intArrayOf(
            Items.MAGIC_POTION1,
            Items.MAGIC_POTION2,
            Items.MAGIC_POTION3,
            Items.MAGIC_POTION4
        ),
        minLevel = 76,
        xpAwarded = 172.5
    )),
    RESTORE(Pot(
        unfinished = Items.HARRALANDER_POTION_UNF,
        secondaries = intArrayOf(Items.RED_SPIDERS_EGGS),
        finished = intArrayOf(
            Items.RESTORE_POTION1,
            Items.RESTORE_POTION2,
            Items.RESTORE_POTION3,
            Items.RESTORE_POTION4
        ),
        minLevel = 22,
        xpAwarded = 62.5
    )),
    SARADOMIN_BREW(Pot(
        unfinished = Items.TOADFLAX_POTION_UNF,
        secondaries = intArrayOf(Items.CRUSHED_NEST),
        finished = intArrayOf(
            Items.SARADOMIN_BREW1,
            Items.SARADOMIN_BREW2,
            Items.SARADOMIN_BREW3,
            Items.SARADOMIN_BREW4
        ),
        minLevel = 81,
        xpAwarded = 180.0
    )),
    ZAMORAK_BREW(Pot(
        unfinished = Items.TORSTOL_POTION_UNF,
        secondaries = intArrayOf(Items.JANGERBERRIES),
        finished = intArrayOf(
            Items.ZAMORAK_BREW1,
            Items.ZAMORAK_BREW2,
            Items.ZAMORAK_BREW3,
            Items.ZAMORAK_BREW4
        ),
        minLevel = 78,
        xpAwarded = 175.0
    )),
    SUPER_COMBAT(Pot(
        unfinished = Items.TORSTOL_POTION_UNF,
        secondaries = intArrayOf(
            Items.SUPER_ATTACK4,
            Items.SUPER_STRENGTH4,
            Items.SUPER_DEFENCE4
        ),
        finished = intArrayOf(
            Items.SUPER_COMBAT_POTION1,
            Items.SUPER_COMBAT_POTION2,
            Items.SUPER_COMBAT_POTION3,
            Items.SUPER_COMBAT_POTION4
        ),
        minLevel = 90,
        xpAwarded = 150.0
    ));

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}
