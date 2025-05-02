package org.alter.plugins.content.skills.thieving.pickpocket

import org.alter.plugins.content.combat.isAttacking
import org.alter.plugins.content.combat.isBeingAttacked

private val PICKPOCKET_ANIMATION = 881
private val GLOVES_OF_SILENCE_BONUS = 5

PickpocketNpc.values.forEach { pickpocketNpc ->
    pickpocketNpc.npcIds.forEach { npcId ->
        on_npc_option(npc = npcId, option = "pickpocket") {
            player.queue {
                pickpocket(npcId, pickpocketNpc)
            }
        }
    }
}
/**
 * @TODO When pickpocketing on "You attempt to pickpocket the Man... The npc faces the player. It should not do that.
 */
suspend fun QueueTask.pickpocket(npcId: Int, npc: PickpocketNpc) {
    val playerThievingLvl = player.getSkills().getCurrentLevel(Skills.THIEVING)
    val npcName = npc.npcName ?: world.definitions.get(NpcDef::class.java, npcId).name
    if (playerThievingLvl < npc.reqLevel) {
        player.message("You need level ${npc.reqLevel} thieving to pick the $npcName's pocket.")
        return
    }
    if (player.isAttacking() || player.isBeingAttacked()) {
        player.message("You can't pickpocket while in combat!")
        return
    }
    if (!player.inventory.hasSpace) {
        player.message("You don't have enough inventory space to pickpocket!")
        return
    }

    //pickpocketing animation and starting message
    player.animate(PICKPOCKET_ANIMATION)
    player.message("You attempt to pickpocket the $npcName...")

    //wait 3 game cycles
    player.lock = LockState.FULL_WITH_ITEM_INTERACTION
    wait(3)
    player.lock = LockState.NONE

    if (getPickpocketSuccess(playerThievingLvl, npc, player)) {
        player.message("...and you succeed!")
        val reward = npc.rewardSet.getRandom()
        player.inventory.add(reward)
        player.addXp(Skills.THIEVING, npc.experience)

    } else {
        //if failed, sends relevant messages
        player.message("You have been Stunned.")

        //damages player for a value in the npc's damage range
        player.hit(npc.damage.random())

        //stuns the player then waits til the stun is done to continue
        player.stun(npc.stunTicks)
    }
}

fun getPickpocketSuccess(
    playerThievingLvl: Int,
    npc: PickpocketNpc,
    player: Player
): Boolean {
    // 1) Basis-kans van 55% → 95%, lineair oplopend tussen npc.reqLevel en max level 99
    val BASE_CHANCE_MIN = 75.0
    val BASE_CHANCE_MAX = 95.0
    val MAX_THIEVING_LEVEL = 99

    // Clamp het niveau binnen [reqLevel..MAX_THIEVING_LEVEL]
    val effectiveLvl = playerThievingLvl.coerceIn(npc.reqLevel, MAX_THIEVING_LEVEL)

    // t = verhouding 0.0 (op reqLevel) → 1.0 (op MAX_THIEVING_LEVEL)
    val t = (effectiveLvl - npc.reqLevel).toDouble() / (MAX_THIEVING_LEVEL - npc.reqLevel)

    // Basis-kans berekenen
    val baseChance = BASE_CHANCE_MIN + t * (BASE_CHANCE_MAX - BASE_CHANCE_MIN)

    // 2) Bonus vanuit Gloves of Silence
    val glovesBonus = if (
        player.hasEquipped(EquipmentType.GLOVES, Items.GLOVES_OF_SILENCE)
    ) GLOVES_OF_SILENCE_BONUS.toDouble() else 0.0

    // 3) Bonus per level boven het vereiste level (bijv. +1% per level)
    val surplusLevels = (playerThievingLvl - npc.reqLevel).coerceAtLeast(0)
    val levelBonus    = surplusLevels.toDouble()

    // 4) Totale kans en clamp op maximaal 100%
    val totalChance = (baseChance + glovesBonus + levelBonus).coerceAtMost(100.0)

    // 5) Roll: slaag als een random getal 0.0–100.0 onder totalChance valt
    return kotlin.random.Random.nextDouble(0.0, 100.0) < totalChance
}
