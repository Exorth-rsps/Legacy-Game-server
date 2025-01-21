package org.alter.plugins.content.npcs.trolls.dad

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.formula.MagicCombatFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy

on_npc_combat(Npcs.DAD_6391) {
    npc.queue {
        combat(this)
    }
}

suspend fun combat(it: QueueTask) {
    val npc = it.npc
    var target = npc.getCombatTarget() ?: return
    var meleeCount = 0 // Counter for consecutive melee attack 3

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(it, target, distance = 6, projectile = false) && npc.isAttackDelayReady()) {
            if (meleeCount in 2..4) {
                when (world.random(0..1)) {
                    0 -> melee_attack(npc, target, exclude = 2) // Exclude melee attack 3
                    1 -> jump_attack(npc, target)
                }
                meleeCount = 0 // Reset the counter after switching attacks
            } else {
                val usedMeleeAttack = melee_attack(npc, target)
                if (usedMeleeAttack == 2) {
                    meleeCount++ // Increment counter for melee attack 3
                } else {
                    meleeCount = 0 // Reset counter if a different attack is used
                }
            }
            npc.postAttackLogic(target)
        }
        it.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun melee_attack(npc: Npc, target: Pawn, exclude: Int? = null): Int {
    val options = listOf(0, 1, 2).filter { it != exclude }
    if (options.isEmpty()) {
        throw IllegalStateException("No valid melee attack options available.")
    }
    val attackType = options[world.random(0 until options.size)]

    when (attackType) {
        0 -> {
            // Melee attack 1
            val minHit = 2
            val maxHit = 23
            npc.prepareAttack(CombatClass.MELEE, CombatStyle.CRUSH, AttackStyle.ACCURATE)
            npc.animate(1142)
            if (MeleeCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
                target.hit(damage = (world.random(minHit..maxHit)), type = HitType.HIT, delay = 1)
            } else {
                target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
            }
        }
        1 -> {
            // Melee attack 2
            val minHit = 3
            val maxHit = 29
            npc.prepareAttack(CombatClass.MELEE, CombatStyle.CRUSH, AttackStyle.AGGRESSIVE)
            npc.animate(1158)
            if (MeleeCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
                target.hit(damage = (world.random(minHit..maxHit)), type = HitType.HIT, delay = 1)
            } else {
                target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
            }
        }
        2 -> {
            // Melee attack 3
            val minHit = 1
            val maxHit = 9
            npc.prepareAttack(CombatClass.MELEE, CombatStyle.CRUSH, AttackStyle.DEFENSIVE)
            npc.animate(284)
            if (MeleeCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
                target.hit(damage = (world.random(minHit..maxHit)), type = HitType.HIT, delay = 1)
            } else {
                target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
            }
        }
    }
    return attackType
}

fun jump_attack(npc: Npc, target: Pawn) {
    val minHit = 5
    val maxHit = 33
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(4991)
    val projectile = npc.createProjectile(target, gfx = 406, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 0)
    //world.spawn(projectile) Need to fixed
    if (MagicCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        target.hit(damage= (world.random(minHit..maxHit)), type = HitType.HIT, delay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1)
    }
}
