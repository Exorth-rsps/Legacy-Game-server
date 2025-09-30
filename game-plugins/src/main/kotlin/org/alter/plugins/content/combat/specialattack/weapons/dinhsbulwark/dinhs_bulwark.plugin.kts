package org.alter.plugins.content.combat.specialattack.weapons.dinhsbulwark

import org.alter.game.model.entity.AreaSound
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.model.entity.Npc
import org.alter.game.model.timer.TimerKey
import org.alter.api.cfg.Animation
import org.alter.api.cfg.Graphic
import org.alter.api.cfg.Items
import org.alter.api.cfg.Sound
import org.alter.api.Skills
import org.alter.api.NpcSkills
import org.alter.plugins.content.combat.Combat
import org.alter.plugins.content.combat.dealHit
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.specialattack.SpecialAttacks

object Bulwark {
    val DEFENCE_TIMER = TimerKey()
}

private val SPECIAL_REQUIREMENT = 50
private val DAMAGE_REDUCTION = 0.5
private val DEFENCE_DURATION = 30

SpecialAttacks.register(Items.DINHS_BULWARK, SPECIAL_REQUIREMENT) {
    player.animate(id = Animation.HUMAN_EASTDOOR_SHOVE)
    player.graphic(id = Graphic.DINHS_BULWARK_SPECIAL)
    world.spawn(AreaSound(tile = player.tile, id = Sound.CLEAVE, radius = 10, volume = 1))

    fun drainOffensiveStats(pawn: Pawn) {
        if (pawn.entityType.isPlayer) {
            val skills = (pawn as Player).getSkills()
            val atk = skills.getCurrentLevel(Skills.ATTACK)
            val str = skills.getCurrentLevel(Skills.STRENGTH)
            val rng = skills.getCurrentLevel(Skills.RANGED)
            val mag = skills.getCurrentLevel(Skills.MAGIC)
            val max = maxOf(atk, str, rng, mag)
            val drainStats = when {
                atk == max && str == max && rng == max && mag == max -> listOf(Skills.ATTACK, Skills.STRENGTH)
                atk == max && str == max -> listOf(Skills.ATTACK, Skills.STRENGTH)
                atk == max -> listOf(Skills.ATTACK)
                str == max -> listOf(Skills.STRENGTH)
                rng == max -> listOf(Skills.RANGED)
                else -> listOf(Skills.MAGIC)
            }
            drainStats.forEach { stat ->
                val drain = (skills.getCurrentLevel(stat) * 0.05).toInt()
                skills.decrementCurrentLevel(stat, drain, capped = false)
            }
        } else if (pawn.entityType.isNpc) {
            val stats = (pawn as Npc).stats
            val atk = stats.getCurrentLevel(NpcSkills.ATTACK)
            val str = stats.getCurrentLevel(NpcSkills.STRENGTH)
            val rng = stats.getCurrentLevel(NpcSkills.RANGED)
            val mag = stats.getCurrentLevel(NpcSkills.MAGIC)
            val max = maxOf(atk, str, rng, mag)
            val drainStats = when {
                atk == max && str == max && rng == max && mag == max -> listOf(NpcSkills.ATTACK, NpcSkills.STRENGTH)
                atk == max && str == max -> listOf(NpcSkills.ATTACK, NpcSkills.STRENGTH)
                atk == max -> listOf(NpcSkills.ATTACK)
                str == max -> listOf(NpcSkills.STRENGTH)
                rng == max -> listOf(NpcSkills.RANGED)
                else -> listOf(NpcSkills.MAGIC)
            }
            drainStats.forEach { stat ->
                val drain = (stats.getCurrentLevel(stat) * 0.05).toInt()
                stats.decrementCurrentLevel(stat, drain, capped = false)
            }
        }
    }

    val isMulti = player.tile.isMulti(world)
    val targets = mutableListOf<Pawn>()
    targets.add(target)
    if (isMulti) {
        world.players.forEach { other ->
            if (other != player && other != target && !other.isDead() && player.tile.isWithinRadius(other.tile, 5)) {
                targets.add(other)
            }
        }
        world.npcs.forEach { npc ->
            if (npc != target && !npc.isDead() && player.tile.isWithinRadius(npc.tile, 5)) {
                targets.add(npc)
            }
        }
    }
    val hitTargets = targets.take(10)

    hitTargets.forEachIndexed { index, pawn ->
        val hits = if (index == 0 && isMulti) 2 else 1
        repeat(hits) { hitIndex ->
            val maxHit = MeleeCombatFormula.getMaxHit(player, pawn)
            val accuracy = MeleeCombatFormula.getAccuracy(player, pawn, specialAttackMultiplier = 1.2)
            val land = accuracy >= world.randomDouble()
            val delay = if (pawn.entityType.isNpc) hitIndex + 1 else 1
            player.dealHit(target = pawn, maxHit = maxHit, landHit = land, delay = delay) { h ->
                if (h.landed && (index != 0 || hitIndex == 0)) {
                    drainOffensiveStats(pawn)
                }
            }
        }
    }

    player.attr[Combat.DAMAGE_TAKE_MULTIPLIER] = DAMAGE_REDUCTION
    player.timers[Bulwark.DEFENCE_TIMER] = DEFENCE_DURATION
}

on_timer(Bulwark.DEFENCE_TIMER) {
    player.attr.remove(Combat.DAMAGE_TAKE_MULTIPLIER)
}
