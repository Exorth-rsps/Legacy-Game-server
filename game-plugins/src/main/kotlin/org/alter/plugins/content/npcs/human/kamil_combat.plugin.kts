package org.alter.plugins.content.npcs.human

import org.alter.game.model.combat.AttackStyle
import org.alter.game.model.combat.CombatClass
import org.alter.game.model.combat.CombatStyle
import org.alter.plugins.content.combat.*
import org.alter.plugins.content.combat.formula.DragonfireFormula
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.strategy.RangedCombatStrategy

on_npc_combat(Npcs.KAMIL) {
    npc.queue {
        combat(this)
    }
}

suspend fun combat(it: QueueTask) {
    val npc = it.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)

        if (npc.moveToAttackRange(it, target, distance = 6, projectile = true)
            && npc.isAttackDelayReady()
        ) {
            // 25% kans: magic attack met freeze
            if (world.chance(1, 4)) {
                freeze_attack(npc, target)

                // 25% kans: melee special met freeze (als melee mogelijk)
            } else if (world.chance(1, 4)
                && npc.canAttackMelee(it, target, moveIfNeeded = false)
            ) {
                melee_freeze_attack(npc, target)

                // anders: gewone melee
            } else {
                melee_attack(npc, target, exclude = null)
            }

            npc.postAttackLogic(target)
        }

        it.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

/**
 * Reguliere melee-aanval met 3 opties.
 * @param exclude: optioneel een index (0..2) om uit te sluiten.
 * @return de gekozen attackType (0,1 of 2)
 */
fun melee_attack(npc: Npc, target: Pawn, exclude: Int? = null): Int {
    val options = listOf(0, 1).filter { it != exclude }
    if (options.isEmpty()) {
        throw IllegalStateException("No valid melee attack options available.")
    }
    val attackType = options[world.random(0 until options.size)]

    when (attackType) {
        0 -> {
            // Melee attack 1
            val minHit = 2
            val maxHit = 8
            npc.prepareAttack(CombatClass.MELEE, CombatStyle.CRUSH, AttackStyle.ACCURATE)
            npc.animate(395)
            if (MeleeCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
                target.hit(damage = world.random(minHit..maxHit), type = HitType.HIT, delay = 1)
            } else {
                target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
            }
        }
        1 -> {
            // Melee attack 2
            val minHit = 3
            val maxHit = 8
            npc.prepareAttack(CombatClass.MELEE, CombatStyle.CRUSH, AttackStyle.AGGRESSIVE)
            npc.animate(7054)
            if (MeleeCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
                target.hit(damage = world.random(minHit..maxHit), type = HitType.HIT, delay = 1)
            } else {
                target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
            }
        }
    }
    return attackType
}

/**
 * Melee-special: kiest één van de drie melee-stijlen (zonder exclude),
 * en voegt bij een hit een freeze toe.
 */
fun melee_freeze_attack(npc: Npc, target: Pawn) {
    // kies een melee-type, maar sluit er geen uit
    val usedType = melee_attack(npc, target, exclude = null)

    // alleen doorgaan als accuracy-check slaagt
    if (world.randomDouble() <= MeleeCombatFormula.getAccuracy(npc, target)) {
        val minHit = 3
        val maxHit = 8
        npc.prepareAttack(CombatClass.MELEE, CombatStyle.CRUSH, AttackStyle.DEFENSIVE)
        npc.animate(7055)

        // tweede accuracy-check voor de daadwerkelijke hit
        if (MeleeCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
            target.hit(damage = world.random(minHit..maxHit), type = HitType.HIT, delay = 1)
            target.freeze(cycles = 6) {
                if (target is Player) {
                    target.message("You have been frozen solid!")
                }
            }
        } else {
            // mislukte hit → block
            target.hit(damage = 0, type = HitType.BLOCK, delay = 1)
        }
    }
}


/**
 * Magic-attack die het doel bevriest.
 */
fun freeze_attack(npc: Npc, target: Pawn) {
    val projectile = npc.createProjectile(
        target, gfx = 395,
        startHeight = 43, endHeight = 31,
        delay = 51, angle = 15, steepness = 127
    )
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(414)
    world.spawn(projectile)
    val hit = npc.dealHit(
        target = target,
        formula = DragonfireFormula(maxHit = 8, minHit = 3),
        delay = RangedCombatStrategy
            .getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    ) {
        if (it.landed()) {
            target.freeze(cycles = 6) {
                if (target is Player) {
                    target.message("You have been frozen.")
                }
            }
        }
    }
    if (hit.blocked()) {
        target.graphic(id = 85, height = 124, delay = hit.getClientHitDelay())
    }
}
