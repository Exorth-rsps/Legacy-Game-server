package org.alter.game.action

import org.alter.game.fs.def.AnimDef
import org.alter.game.model.LockState
import org.alter.game.model.attr.KILLER_ATTR
import org.alter.game.model.entity.*
import org.alter.game.model.queue.QueueTask
import org.alter.game.model.queue.TaskPriority
import org.alter.game.plugin.Plugin
import org.alter.game.service.log.LoggerService
import java.lang.ref.WeakReference

/**
 * This class is responsible for handling npc death events.
 *
 * @author Tom <rspsmods@gmail.com>
 */
object NpcDeathAction {

    var deathPlugin: Plugin.() -> Unit = {
        val npc = ctx as Npc
        if (!npc.world.plugins.executeNpcFullDeath(npc)) {
            npc.interruptQueues()
            npc.stopMovement()
            npc.lock()
            npc.queue(TaskPriority.STRONG) {
                death(npc)
            }
        }
    }

    suspend fun QueueTask.death(npc: Npc) {
        val world = npc.world
        val deathAnimation = npc.combatDef.deathAnimation
        val deathSound = npc.combatDef.defaultDeathSound
        val respawnDelay = npc.combatDef.respawnDelay
        var killer: Pawn? = null

        // Bepaal de killer en log de kill
        npc.damageMap.getMostDamage()?.let {
            if (it is Player) {
                killer = it
                world.getService(LoggerService::class.java, searchSubclasses = true)
                    ?.logNpcKill(it, npc)
            }
            npc.attr[KILLER_ATTR] = WeakReference(it)
        }

        world.plugins.executeNpcPreDeath(npc)
        npc.resetFacePawn()
        world.plugins.executeSlayerLogic(npc)

        // Speel sound af
        if (npc.combatDef.defaultDeathSoundArea) {
            world.spawn(AreaSound(
                npc.tile, deathSound,
                npc.combatDef.defaultDeathSoundRadius,
                npc.combatDef.defaultDeathSoundVolume
            ))
        } else {
            (killer as? Player)
                ?.playSound(deathSound, npc.combatDef.defaultDeathSoundVolume)
        }

        // Death‐animaties
        deathAnimation.forEach { anim ->
            val def = npc.world.definitions.get(AnimDef::class.java, anim)
            npc.animate(def.id)
            wait(def.cycleLength + 1)
        }

        npc.animate(-1)
        world.plugins.executeNpcDeath(npc)

        // Respawn of remove
        if (npc.respawns) {
            npc.invisible = true
            npc.reset()                // <-- hier clearen we damageMap
            wait(respawnDelay)
            npc.invisible = false
            world.plugins.executeNpcSpawn(npc)
        } else {
            // Voor non‐respawn NPC’s is clear optioneel, want de instance wordt verwijderd
            npc.damageMap.clear()
            world.remove(npc)
        }
    }

    private fun Npc.reset() {
        // 1) Maak oude damageEntries schoon
        damageMap.clear()

        // 2) Reset overige state
        lock = LockState.NONE
        tile = spawnTile
        setTransmogId(-1)

        attr.clear()
        timers.clear()
        world.setNpcDefaults(this)
    }
}
