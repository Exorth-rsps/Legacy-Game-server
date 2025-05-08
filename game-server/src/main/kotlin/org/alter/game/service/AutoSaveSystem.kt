package org.alter.game.service

import org.alter.game.Server.Companion.logger
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.service.serializer.PlayerSerializerService
import kotlin.system.measureTimeMillis

/**
 * AutoSaveService die via game-ticks wordt aangeroepen.
 * Roep execute() handmatig vanuit World.cycle() op.
 */
class AutoSaveSystem(private val world: World) {

    // Interval in milliseconden (5 minuten)
    private val saveIntervalMs = 5 * 60 * 1000L
    // Tijdstip van de laatste save
    private var lastSaveTime = System.currentTimeMillis()

    /**
     * Sla, indien 5 minuten verstreken, alle ingelogde spelers op.
     * Deze methode moet iedere game-tick worden aangeroepen.
     */
    fun execute() {
        val now = System.currentTimeMillis()
        if (now - lastSaveTime < saveIntervalMs) return
        lastSaveTime = now

        val serializer = world.getService(
            PlayerSerializerService::class.java,
            searchSubclasses = true
        ) ?: return

        // Verzamel alle ingelogde spelers
        val playersToSave = mutableListOf<Client>()
        // PawnList toont spelers via index; gebruik capacity en get(i)
        for (i in 0 until world.players.capacity) {
            val p = world.players[i]
            if (p is Client) playersToSave.add(p)
        }
        val count = playersToSave.size

        // Voer save uit en meet tijd
        val duration = measureTimeMillis {
            playersToSave.forEach { client ->
                serializer.saveClientData(client)
            }
        }
        logger.info("Autosaved $count players.")
    }
}
