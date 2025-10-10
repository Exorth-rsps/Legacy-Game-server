package org.alter.plugins.content.npcs.human

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.MagicCombatFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy
import org.alter.api.ProjectileType
import org.alter.api.HitType
import org.alter.api.cfg.Animation
import org.alter.api.cfg.Graphic
import org.alter.api.cfg.Npcs

on_npc_combat(Npcs.ANCIENT_WIZARD) {
    npc.queue {
        ancientWizardCombat(this)
    }
}

suspend fun ancientWizardCombat(task: QueueTask) {
    val npc = task.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(task, target, distance = 10, projectile = true) && npc.isAttackDelayReady()) {
            iceBlitz(npc, target)
            npc.postAttackLogic(target)
        }
        task.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun iceBlitz(npc: Npc, target: Pawn) {
    val minHit = 4
    val maxHit = 18
    val projectile = npc.createProjectile(target, gfx = Graphic.ICE_RUSH_PROJECTILE, type = ProjectileType.MAGIC)
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(Animation.ANCIENT_SPELL_SINGLE_CAST)
    world.spawn(projectile)
    val hitDelay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    if (MagicCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        val damage = world.random(minHit..maxHit)
        target.hit(damage = damage, type = HitType.HIT, delay = hitDelay)
        target.graphic(id = Graphic.ICE_BLITZ_HIT, height = 124, delay = hitDelay)
        if (target is Player) {
            target.freeze(cycles = 25) {
                target.message("You feel a freezing cold run down your spine!")
            }
        } else {
            target.freeze(cycles = 25)
        }
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = hitDelay)
    }
}
