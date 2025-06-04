package org.alter.plugins.content.npcs.human

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.queue.QueueTask
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.MagicCombatFormula
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy
import org.alter.api.HitType
import org.alter.api.cfg.Animation
import org.alter.api.cfg.Graphic
import org.alter.api.cfg.Npcs

on_npc_combat(Npcs.ELDER_CHAOS_DRUID) {
    npc.queue {
        combat(this)
    }
}

suspend fun combat(it: QueueTask) {
    val npc = it.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(it, target, distance = 8, projectile = true) && npc.isAttackDelayReady()) {
            val roll = world.random(100)
            when {
                roll < 10 -> flamesOfZamorak(npc, target)
                roll < 55 -> bloodBarrage(npc, target)
                npc.canAttackMelee(it, target, moveIfNeeded = false) -> meleeAttack(npc, target)
                else -> bloodBarrage(npc, target)
            }
            npc.postAttackLogic(target)
        }
        it.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun flamesOfZamorak(npc: Npc, target: Pawn) {
    val minHit = 12
    val maxHit = 31
    val projectile = npc.createProjectile(
        target,
        gfx = Graphic.FLAMES_OF_ZAMORAK,
        startHeight = 43,
        endHeight = 31,
        delay = 51,
        angle = 15,
        steepness = 127
    )
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(Animation.GOD_SPELL)
    npc.graphic(Graphic.FLAMES_OF_ZAMORAK)
    //world.spawn(projectile)
    val hitDelay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    if (MagicCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        target.hit(damage = world.random(minHit..maxHit), type = HitType.HIT, delay = hitDelay)
        target.graphic(id = Graphic.FLAMES_OF_ZAMORAK, height = 124, delay = hitDelay)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = hitDelay)
    }
}

fun bloodBarrage(npc: Npc, target: Pawn) {
    val minHit = 4
    val maxHit = 20
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(Animation.ANCIENT_SPELL_MULTI_CAST)
    val hitDelay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    val accuracy = MagicCombatFormula.getAccuracy(npc, target)
    if (accuracy >= world.randomDouble()) {
        val damage = world.random(minHit..maxHit)
        target.hit(damage = damage, type = HitType.HIT, delay = hitDelay)
        target.graphic(id = Graphic.BLOOD_BARRAGE_HIT, height = 124, delay = hitDelay)
        val heal = damage / 4
        if (heal > 0) {
            npc.setCurrentHp((npc.getCurrentHp() + heal).coerceAtMost(npc.getMaxHp()))
            npc.hit(damage = heal, type = HitType.NPC_HEAL)
        }
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = hitDelay)
    }
}

fun meleeAttack(npc: Npc, target: Pawn) {
    val minHit = 4
    val maxHit = 15
    npc.prepareAttack(CombatClass.MELEE, CombatStyle.SLASH, AttackStyle.ACCURATE)
    npc.animate(Animation.HUMAN_PUNCH)
    if (MeleeCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        target.hit(damage = world.random(minHit..maxHit), type = HitType.HIT, delay = 1)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
    }
}
