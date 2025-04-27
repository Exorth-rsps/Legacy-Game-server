package org.alter.plugins.content.npcs.dragons

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.DragonfireFormula
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy

on_npc_combat(
    Npcs.BLUE_DRAGON
) {
    npc.queue {
        combat(this)
    }
}

suspend fun combat(it: QueueTask) {
    val npc = it.npc
    var target = npc.getCombatTarget() ?: return

    var meleeCount = 0
    // 1 fire attack after a random 5-8 melee attacks
    var nextFireAt = world.random(3) + 4

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(it, target, distance = 6, projectile = true) && npc.isAttackDelayReady()) {
            if (meleeCount < nextFireAt && npc.canAttackMelee(it, target, moveIfNeeded = false)) {
                melee_attack(npc, target)
                meleeCount++
            } else {
                fire_attack(npc, target)
                // reset counter and choose a new threshold
                meleeCount = 0
                nextFireAt = world.random(3) + 5
            }
            npc.postAttackLogic(target)
        }
        it.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun melee_attack(npc: Npc, target: Pawn) {
    // Single melee attack type: use claw
    npc.prepareAttack(CombatClass.MELEE, CombatStyle.SLASH, AttackStyle.AGGRESSIVE)
    npc.animate(80)
    if (MeleeCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        target.hit(world.random(10), type = HitType.HIT, delay = 1)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
    }
}

fun fire_attack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(
        target,
        gfx = 393,
        startHeight = 43,
        endHeight = 31,
        delay = 51,
        angle = 15,
        steepness = 127
    )
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(82)
    world.spawn(projectile)
    npc.dealHit(
        target = target,
        formula = DragonfireFormula(maxHit = 50),
        delay = RangedCombatStrategy.getHitDelay(
            npc.getFrontFacingTile(target),
            target.getCentreTile()
        ) - 1
    )
}
