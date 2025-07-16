package org.alter.plugins.content.combat.specialattack.weapons.dinhsbulwark

import org.alter.game.model.entity.AreaSound
import org.alter.game.model.entity.Pawn
import org.alter.game.model.timer.TimerKey
import org.alter.api.cfg.Animation
import org.alter.api.cfg.Graphic
import org.alter.api.cfg.Items
import org.alter.api.cfg.Sound
import org.alter.api.cfg.Varp
import org.alter.plugins.content.combat.Combat
import org.alter.plugins.content.combat.dealHit
import org.alter.plugins.content.combat.formula.MeleeCombatFormula
import org.alter.plugins.content.combat.specialattack.SpecialAttacks

object Bulwark {
    val DEFENCE_TIMER = TimerKey()
}

private const val SPECIAL_REQUIREMENT = 50
private const val DAMAGE_REDUCTION = 0.5
private const val DEFENCE_DURATION = 30

SpecialAttacks.register(Items.DINHS_BULWARK, SPECIAL_REQUIREMENT) {
    player.animate(id = Animation.HUMAN_EASTDOOR_SHOVE)
    player.graphic(id = Graphic.DINHS_BULWARK_SPECIAL)
    world.spawn(AreaSound(tile = player.tile, id = Sound.CLEAVE, radius = 10, volume = 1))

    val targets = mutableListOf<Pawn>()
    world.players.forEach { other ->
        if (other != player && !other.isDead() && player.tile.isWithinRadius(other.tile, 1)) {
            targets.add(other)
        }
    }
    world.npcs.forEach { npc ->
        if (!npc.isDead() && player.tile.isWithinRadius(npc.tile, 1)) {
            targets.add(npc)
        }
    }

    targets.forEach { t ->
        val maxHit = MeleeCombatFormula.getMaxHit(player, t)
        val accuracy = MeleeCombatFormula.getAccuracy(player, t)
        val landHit = accuracy >= world.randomDouble()
        player.dealHit(target = t, maxHit = maxHit, landHit = landHit, delay = 1)
    }

    player.attr[Combat.DAMAGE_TAKE_MULTIPLIER] = DAMAGE_REDUCTION
    player.setVarp(Varp.WEAPON_STANCE_BULWARK, 1)
    player.timers[Bulwark.DEFENCE_TIMER] = DEFENCE_DURATION
}

on_timer(Bulwark.DEFENCE_TIMER) {
    player.attr.remove(Combat.DAMAGE_TAKE_MULTIPLIER)
    player.setVarp(Varp.WEAPON_STANCE_BULWARK, 0)
}
