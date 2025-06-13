package org.alter.plugins.content.npcs.undead

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.queue.QueueTask
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.formula.MagicCombatFormula
import org.alter.plugins.content.combat.strategy.MagicCombatStrategy

on_npc_combat(Npcs.VYREWATCH_8256) {
    npc.queue { combat(this) }
}

suspend fun combat(task: QueueTask) {
    val npc = task.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(task, target, distance = 6, projectile = true) && npc.isAttackDelayReady()) {
            if (world.random(0..1) == 0) {
                meleeAttack(npc, target)
            } else {
                magicAttack(npc, target)
            }
            npc.postAttackLogic(target)
        }
        task.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun meleeAttack(npc: Npc, target: Pawn) {
    val maxHit = 18
    npc.prepareAttack(CombatClass.MELEE, CombatStyle.SLASH, AttackStyle.ACCURATE)
    npc.animate(Animation.VAMPIRE_ATTACK)
    val accuracy = MeleeCombatFormula.getAccuracy(npc, target)
    val land = accuracy >= world.randomDouble()
    npc.dealHit(target = target, maxHit = maxHit, landHit = land, delay = 1)
}

fun magicAttack(npc: Npc, target: Pawn) {
    val maxHit = 14
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(Animation.VAMPIRE_ATTACK)
    npc.createProjectile(target, gfx = 373, startHeight = 40, endHeight = 31, delay = 51, angle = 15, steepness = 0)
    val hitDelay = MagicCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    if (MagicCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        target.hit(damage = world.random(0..maxHit), type = HitType.HIT, delay = hitDelay)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = hitDelay)
    }
}

