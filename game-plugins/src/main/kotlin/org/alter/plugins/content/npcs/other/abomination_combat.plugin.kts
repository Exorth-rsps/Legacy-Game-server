package org.alter.plugins.content.npcs.other

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.formula.RangedCombatFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy

on_npc_combat(Npcs.ABOMINATION_8262) {
    npc.queue {
        combat(this)
    }
}

suspend fun combat(it: QueueTask) {
    val npc = it.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(it, target, distance = 6, projectile = false) && npc.isAttackDelayReady()) {
            if (world.random(0..2) != 0 && npc.canAttackMelee(it, target, moveIfNeeded = false)) {
                meleeAttack(npc, target)
            } else {
                rangeAttack(npc, target)
            }
            npc.postAttackLogic(target)
        }
        it.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun meleeAttack(npc: Npc, target: Pawn) {
    val minHit = 5
    val maxHit = 17
    npc.prepareAttack(CombatClass.MELEE, CombatStyle.CRUSH, AttackStyle.ACCURATE)
    npc.animate(Animation.ABOMINATION_ATTACK)
    if (MeleeCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        target.hit(damage = world.random(minHit..maxHit), type = HitType.HIT, delay = 1)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
    }
}

fun rangeAttack(npc: Npc, target: Pawn) {
    val minHit = 8
    val maxHit = 25
    npc.prepareAttack(CombatClass.RANGED, CombatStyle.RANGED, AttackStyle.ACCURATE)
    npc.animate(Animation.ABOMINATION_RANGE_ATTACK)
    npc.createProjectile(target, gfx = 406, startHeight = 43, endHeight = 31, delay = 51, angle = 15, steepness = 0)
    val hitDelay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    if (RangedCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        target.hit(damage = world.random(minHit..maxHit), type = HitType.HIT, delay = hitDelay)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = hitDelay)
    }
}
