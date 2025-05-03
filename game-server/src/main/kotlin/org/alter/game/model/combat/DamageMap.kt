package org.alter.game.model.combat

import org.alter.game.model.EntityType
import org.alter.game.model.entity.Pawn

/**
 * Represents a map of hits from different [Pawn]s and their information,
 * met automatische eviction van oude entries na een TTL (standaard 10 minuten).
 */
class DamageMap(
    /** Time‐to‐live in milliseconden; standaard: 10 min = 600_000 ms */
    private val ttlMs: Long = 10 * 60 * 1000L,
    /** Initieel capaciteit, afgestemd op kleine aantallen attackers */
    initialCapacity: Int = 4
) {

    // Gewone HashMap, vullen we zelf en evicten we handmatig
    private val map = HashMap<Pawn, DamageStack>(initialCapacity)

    /** Hulp om te kijken of een entry nog binnen de TTL valt */
    private fun isValid(stack: DamageStack): Boolean =
        System.currentTimeMillis() - stack.lastHit <= ttlMs

    /** Verwijder alle entries ouder dan `ttlMs` */
    private fun evictExpired() {
        val cutoff = System.currentTimeMillis() - ttlMs
        map.entries.removeIf { it.value.lastHit < cutoff }
    }

    /** Haal de DamageStack voor [pawn], mits die nog niet vervallen is */
    operator fun get(pawn: Pawn): DamageStack? {
        evictExpired()
        val stack = map[pawn]
        return if (stack != null && isValid(stack)) stack else null
    }

    /**
     * Voeg [damage] toe voor [pawn],
     * en evict eerst oude entries.
     */
    fun add(pawn: Pawn, damage: Int) {
        evictExpired()
        val current = map[pawn]?.totalDamage ?: 0
        map[pawn] = DamageStack(current + damage, System.currentTimeMillis())
    }

    /** Maak de hele map in één keer leeg */
    fun clear() {
        map.clear()
    }

    /**
     * Geef alle DamageStacks van Pawns van het gegeven [type],
     * zonder vervallen entries.
     */
    fun getAll(
        type: EntityType,
        timeFrameMs: Long? = null
    ): Collection<DamageStack> {
        // eerst verouderde entries eruit
        evictExpired()

        // filter op entity-type en (optioneel) op timeframe
        return map
            .filter { (key, stack) ->
                key.entityType == type
                        && (timeFrameMs == null
                        || System.currentTimeMillis() - stack.lastHit < timeFrameMs)
            }
            .values
    }

    /** Totaal damage door [pawn], of 0 als verlopen/geen entry */
    fun getDamageFrom(pawn: Pawn): Int {
        evictExpired()
        return map[pawn]?.totalDamage ?: 0
    }

    /** Pawn met de meeste damage, na evict van verlopen entries */
    fun getMostDamage(): Pawn? {
        evictExpired()
        return map.maxByOrNull { it.value.totalDamage }?.key
    }

    /**
     * Pawn van het gegeven [type] met de meeste damage,
     * na evict van verlopen entries.
     */
    fun getMostDamage(type: EntityType): Pawn? {
        evictExpired()
        return map
            .filter { it.key.entityType == type }
            .maxByOrNull { it.value.totalDamage }
            ?.key
    }

    /** Data‐klasse met cumulatieve damage en tijd van laatste hit */
    data class DamageStack(val totalDamage: Int, val lastHit: Long)
}
