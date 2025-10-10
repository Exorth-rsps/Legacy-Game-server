package org.alter.plugins.content.npcs.human

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.queue.QueueTask
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.MagicCombatFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy
import org.alter.api.ProjectileType
import org.alter.api.HitType
import org.alter.api.cfg.Animation
import org.alter.api.cfg.Graphic
import org.alter.api.cfg.Npcs

on_npc_combat(Npcs.NECROMANCER_11088) {
    npc.queue {
        combatLoop(this)
    }
}

suspend fun combatLoop(task: QueueTask) {
    val npc = task.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(task, target, distance = 8, projectile = true) && npc.isAttackDelayReady()) {
            fireStrike(npc, target)
            npc.postAttackLogic(target)
        }
        task.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun fireStrike(npc: Npc, target: Pawn) {
    val minHit = 1
    val maxHit = 7
    val projectile = npc.createProjectile(target, gfx = Graphic.FIRE_STRIKE_PROJECTILE, type = ProjectileType.MAGIC)
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(Animation.STAFF_MAGIC_SPELL_CAST)
    npc.graphic(Graphic.FIRE_STRIKE_CAST, height = 124)
    world.spawn(projectile)
    val hitDelay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    val accuracy = MagicCombatFormula.getAccuracy(npc, target)
    if (accuracy >= world.randomDouble()) {
        val damage = world.random(minHit..maxHit)
        target.hit(damage = damage, type = HitType.HIT, delay = hitDelay)
        target.graphic(id = Graphic.FIRE_STRIKE_HIT, height = 124, delay = hitDelay)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = hitDelay)
    }
}
