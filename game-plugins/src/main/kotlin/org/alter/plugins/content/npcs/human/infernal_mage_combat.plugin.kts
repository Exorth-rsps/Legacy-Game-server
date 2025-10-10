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

val infernalMageIds = intArrayOf(
    Npcs.INFERNAL_MAGE_443,
    Npcs.INFERNAL_MAGE_444,
    Npcs.INFERNAL_MAGE_445,
    Npcs.INFERNAL_MAGE_446,
    Npcs.INFERNAL_MAGE_447
)
infernalMageIds.forEach { npcId ->
    on_npc_combat(npcId) {
        npc.queue {
            infernalMageCombat(this)
        }
    }
}

suspend fun infernalMageCombat(task: QueueTask) {
    val npc = task.npc
    var target = npc.getCombatTarget() ?: return

    while (npc.canEngageCombat(target)) {
        npc.facePawn(target)
        if (npc.moveToAttackRange(task, target, distance = 8, projectile = true) && npc.isAttackDelayReady()) {
            fireBlast(npc, target)
            npc.postAttackLogic(target)
        }
        task.wait(1)
        target = npc.getCombatTarget() ?: break
    }

    npc.resetFacePawn()
    npc.removeCombatTarget()
}

fun fireBlast(npc: Npc, target: Pawn) {
    val minHit = 1
    val maxHit = 8
    val projectile = npc.createProjectile(target, gfx = Graphic.FIRE_BLAST_PROJECTILE, type = ProjectileType.MAGIC)
    npc.prepareAttack(CombatClass.MAGIC, CombatStyle.MAGIC, AttackStyle.ACCURATE)
    npc.animate(Animation.STAFF_MAGIC_SPELL_CAST)
    npc.graphic(Graphic.FIRE_BLAST_CAST, height = 124)
    world.spawn(projectile)
    val hitDelay = RangedCombatStrategy.getHitDelay(npc.getFrontFacingTile(target), target.getCentreTile()) - 1
    if (MagicCombatFormula.getAccuracy(npc, target) >= world.randomDouble()) {
        val damage = world.random(minHit..maxHit)
        target.hit(damage = damage, type = HitType.HIT, delay = hitDelay)
        target.graphic(id = Graphic.FIRE_BLAST_HIT, height = 124, delay = hitDelay)
    } else {
        target.hit(damage = 0, type = HitType.BLOCK, delay = hitDelay)
    }
}
