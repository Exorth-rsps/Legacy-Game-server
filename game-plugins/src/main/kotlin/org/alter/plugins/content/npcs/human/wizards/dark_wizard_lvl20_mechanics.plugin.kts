package org.alter.plugins.content.npcs.human.wizards

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.DragonfireFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy

on_npc_combat(Npcs.DARK_WIZARD) {
    npc.queue {
        combat(this)
    }
}

suspend fun combat(it: QueueTask) {
    val npc = it.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(it, target, distance = 10, projectile = true) && npc.isAttackDelayReady()) {
            fire_attack(npc, target)
            npc.postAttackLogic(target)
        }
        it.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}


fun fire_attack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(
        target = target,
        gfx = 124,
        startHeight = 43,
        endHeight = 31,
        delay = 51,
        angle = 15,
        steepness = 127
    )
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(Animation.MAGIC_WAVE_CAST)

    world.spawn(projectile)
    npc.dealHit(
        target = target,
        formula = DragonfireFormula(maxHit = 6),
        delay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    )
}

