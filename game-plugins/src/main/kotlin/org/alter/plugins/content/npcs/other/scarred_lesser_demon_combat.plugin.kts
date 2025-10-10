package org.alter.plugins.content.npcs.other

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.queue.QueueTask
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.MagicCombatFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy
import org.alter.api.HitType
import org.alter.api.cfg.Animation
import org.alter.api.cfg.Graphic
import org.alter.api.cfg.Npcs

scarredLesserDemonIds.forEach { npcId ->
    on_npc_combat(npcId) {
        npc.queue {
            scarredLesserDemonCombat(this)
        }
    }
}

suspend fun scarredLesserDemonCombat(task: QueueTask) {
    val npc = task.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(task, target, distance = 8, projectile = true) && npc.isAttackDelayReady()) {
            bloodBurst(npc, target)
            npc.postAttackLogic(target)
        }
        task.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun bloodBurst(npc: Npc, target: Pawn) {
    val minHit = 5
    val maxHit = 21
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(Animation.ANCIENT_SPELL_MULTI_CAST)
    val hitDelay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    val accuracy = MagicCombatFormula.getAccuracy(npc, target)
    if (accuracy >= world.randomDouble()) {
        val damage = world.random(minHit..maxHit)
        target.hit(damage = damage, type = HitType.HIT, delay = hitDelay)
        target.graphic(id = Graphic.BLOOD_BURST_HIT, height = 124, delay = hitDelay)
        val heal = damage / 4
        if (heal > 0) {
            npc.setCurrentHp((npc.getCurrentHp() + heal).coerceAtMost(npc.getMaxHp()))
            npc.hit(heal, HitType.NPC_HEAL)
        }
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = hitDelay)
    }
}
