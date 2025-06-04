package org.alter.plugins.content.npcs.human

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.formula.MagicCombatFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy
import org.alter.api.ProjectileType
import org.alter.api.HitType
import org.alter.api.cfg.Animation
import org.alter.api.cfg.Graphic

on_npc_combat(Npcs.UNGADULU) {
    npc.queue {
        combat(this)
    }
}

suspend fun combat(it: QueueTask) {
    val npc = it.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(it, target, distance = 6, projectile = true) && npc.isAttackDelayReady()) {
            when (world.random(2)) {
                0 -> meleeAttack(npc, target)
                1 -> fireAttack(npc, target)
                2 -> healSelf(npc)
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
    val minHit = 2
    val maxHit = 12
    npc.prepareAttack(CombatClass.MELEE, CombatStyle.CRUSH, AttackStyle.ACCURATE)
    npc.animate(Animation.HUMAN_STAFF_BASH)
    if (MeleeCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        target.hit(damage = world.random(minHit..maxHit), type = HitType.HIT, delay = 1)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
    }
}

fun fireAttack(npc: Npc, target: Pawn) {

    val minHit = 4
    val maxHit = 16
    val projectile = npc.createProjectile(target, gfx = Graphic.FIRE_WAVE_PROJECTILE, type = ProjectileType.MAGIC)
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(Animation.HUMAN_STAFF_BASH)
    npc.graphic(Graphic.FIRE_WAVE_CAST)
    world.spawn(projectile)
    val hitDelay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1

    if (MagicCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        target.hit(damage = world.random(minHit..maxHit), type = HitType.HIT, delay = hitDelay)
        target.graphic(id = Graphic.FIRE_WAVE_HIT, height = 124, delay = hitDelay)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = hitDelay)

    }
}

fun healSelf(npc: Npc) {
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(Animation.MONK_HEAL)
    npc.graphic(108, 10)
    val heal = world.random(5..10)
    val newHp = (npc.getCurrentHp() + heal).coerceAtMost(npc.getMaxHp())
    npc.setCurrentHp(newHp)
    npc.hit(damage = heal, type = HitType.NPC_HEAL)
}

