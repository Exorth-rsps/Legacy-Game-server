package org.alter.plugins.content.npcs.barrows

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.game.model.entity.Npc
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.api.HitType
import org.alter.api.ProjectileType
import org.alter.api.Skills
import org.alter.api.cfg.Animation
import org.alter.api.cfg.Graphic
import org.alter.api.cfg.Npcs
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.MagicCombatFormula
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.formula.RangedCombatFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy

on_npc_combat(Npcs.DHAROK_THE_WRETCHED) {
    npc.queue { combatDharok(this) }
}

on_npc_combat(Npcs.VERAC_THE_DEFILED) {
    npc.queue { combatVerac(this) }
}

on_npc_combat(Npcs.AHRIM_THE_BLIGHTED) {
    npc.queue { combatAhrim(this) }
}

on_npc_combat(Npcs.GUTHAN_THE_INFESTED) {
    npc.queue { combatGuthan(this) }
}

on_npc_combat(Npcs.KARIL_THE_TAINTED) {
    npc.queue { combatKaril(this) }
}

on_npc_combat(Npcs.TORAG_THE_CORRUPTED) {
    npc.queue { combatTorag(this) }
}

suspend fun combatDharok(task: QueueTask) {
    val npc = task.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(task, target, distance = 1, projectile = false) && npc.isAttackDelayReady()) {
            dharokAttack(npc, target)
            npc.postAttackLogic(target)
        }
        task.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun dharokAttack(npc: Npc, target: Pawn) {
    val baseMax = 31
    val missing = npc.getMaxHp() - npc.getCurrentHp()
    val multiplier = 1.0 + missing / npc.getMaxHp().toDouble()
    val maxHit = (baseMax * multiplier).toInt()
    npc.prepareAttack(CombatClass.MELEE, CombatStyle.SLASH, AttackStyle.AGGRESSIVE)
    npc.animate(Animation.HUMAN_DHAROKS_GREATAXE_SWING)
    val accuracy = MeleeCombatFormula.getAccuracy(npc, target)
    val land = accuracy >= npc.world.randomDouble()
    npc.dealHit(target = target, maxHit = maxHit, landHit = land, delay = 1)
}

suspend fun combatVerac(task: QueueTask) {
    val npc = task.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(task, target, distance = 1, projectile = false) && npc.isAttackDelayReady()) {
            veracAttack(npc, target)
            npc.postAttackLogic(target)
        }
        task.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun veracAttack(npc: Npc, target: Pawn) {
    val maxHit = 28
    npc.prepareAttack(CombatClass.MELEE, CombatStyle.CRUSH, AttackStyle.AGGRESSIVE)
    npc.animate(Animation.HUMAN_VERACS_FLAIL_ATTACK)
    val ignore = npc.world.chance(1, 4)
    val accuracy = if (ignore) 1.0 else MeleeCombatFormula.getAccuracy(npc, target)
    val land = accuracy >= npc.world.randomDouble()
    npc.dealHit(target = target, maxHit = maxHit, landHit = land, delay = 1)
}

suspend fun combatAhrim(task: QueueTask) {
    val npc = task.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(task, target, distance = 8, projectile = true) && npc.isAttackDelayReady()) {
            ahrimAttack(npc, target)
            npc.postAttackLogic(target)
        }
        task.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun ahrimAttack(npc: Npc, target: Pawn) {
    val maxHit = 20
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(Animation.HUMAN_AHRIMS_STAFF_ATTACK)
    val hitDelay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    val accuracy = MagicCombatFormula.getAccuracy(npc, target)
    val land = accuracy >= npc.world.randomDouble()
    npc.dealHit(target = target, maxHit = maxHit, landHit = land, delay = hitDelay) {
        if (it.landed() && npc.world.chance(1, 4) && target is Player) {
            target.getSkills().alterCurrentLevel(Skills.STRENGTH, -5)
            target.message("You feel weakened!")
        }
    }
}

suspend fun combatGuthan(task: QueueTask) {
    val npc = task.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(task, target, distance = 1, projectile = false) && npc.isAttackDelayReady()) {
            guthanAttack(npc, target)
            npc.postAttackLogic(target)
        }
        task.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun guthanAttack(npc: Npc, target: Pawn) {
    val maxHit = 23
    npc.prepareAttack(CombatClass.MELEE, CombatStyle.STAB, AttackStyle.AGGRESSIVE)
    npc.animate(Animation.HUMAN_GUTHANS_WARSPEAR_STAB)
    val accuracy = MeleeCombatFormula.getAccuracy(npc, target)
    val land = accuracy >= npc.world.randomDouble()
    npc.dealHit(target = target, maxHit = maxHit, landHit = land, delay = 1) {
        if (it.landed() && npc.world.chance(1, 4)) {
            val heal = it.hit.hitmarks.sumOf { mark -> mark.damage }
            if (heal > 0) {
                npc.setCurrentHp((npc.getCurrentHp() + heal).coerceAtMost(npc.getMaxHp()))
                npc.hit(heal, HitType.NPC_HEAL)
            }
        }
    }
}

suspend fun combatKaril(task: QueueTask) {
    val npc = task.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(task, target, distance = 7, projectile = true) && npc.isAttackDelayReady()) {
            karilAttack(npc, target)
            npc.postAttackLogic(target)
        }
        task.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun karilAttack(npc: Npc, target: Pawn) {
    val maxHit = 20
    val projectile = npc.createProjectile(target, gfx = Graphic.KARILS_TAINTED_SHOT, type = ProjectileType.BOLT)
    npc.prepareAttack(CombatClass.RANGED, CombatStyle.RANGED, AttackStyle.ACCURATE)
    npc.animate(Animation.HUMAN_KARILS_CROSSBOW_ATTACK)
    npc.world.spawn(projectile)
    val hitDelay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    val accuracy = RangedCombatFormula.getAccuracy(npc, target)
    val land = accuracy >= npc.world.randomDouble()
    npc.dealHit(target = target, maxHit = maxHit, landHit = land, delay = hitDelay) {
        if (it.landed() && npc.world.chance(1, 4) && target is Player) {
            target.getSkills().alterCurrentLevel(Skills.AGILITY, -2)
            target.message("You feel your legs weaken!")
        }
    }
}

suspend fun combatTorag(task: QueueTask) {
    val npc = task.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(task, target, distance = 1, projectile = false) && npc.isAttackDelayReady()) {
            toragAttack(npc, target)
            npc.postAttackLogic(target)
        }
        task.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun toragAttack(npc: Npc, target: Pawn) {
    val maxHit = 21
    npc.prepareAttack(CombatClass.MELEE, CombatStyle.CRUSH, AttackStyle.AGGRESSIVE)
    npc.animate(Animation.HUMAN_TORAGS_HAMMERS_SWING)
    val accuracy = MeleeCombatFormula.getAccuracy(npc, target)
    val land = accuracy >= npc.world.randomDouble()
    npc.dealHit(target = target, maxHit = maxHit, landHit = land, delay = 1) {
        if (it.landed() && npc.world.chance(1, 4) && target is Player) {
            target.runEnergy = (target.runEnergy - 200.0).coerceAtLeast(0.0)
            target.sendRunEnergy(target.runEnergy.toInt())
            target.message("You feel drained of energy!")
        }
    }
}
