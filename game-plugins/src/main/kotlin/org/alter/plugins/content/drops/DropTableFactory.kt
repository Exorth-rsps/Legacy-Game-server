package org.alter.plugins.content.drops

import org.alter.game.model.Tile
import org.alter.game.model.World
import org.alter.game.model.entity.GroundItem
import org.alter.game.model.entity.Pawn
import org.alter.game.model.entity.Player
import org.alter.game.model.item.Item
import java.security.SecureRandom

/**
 * The name of a table that always drops all of its items
 */
const val GUARANTEED_TABLE_NAME = "guaranteed"

/**
 * A utility class that assists with building dynamic drop tables.
 */
object DropTableFactory {

    /** De drop-tabellen per npc. */
    private val tables = DropTableType.values().associateWith {
        HashMap<Int, DropTableBuilder.() -> Unit>()
    }

    /** PRNG voor willekeur. */
    var prng = SecureRandom()

    /** Helper om een DSL-block door te geven. */
    fun build(init: DropTableBuilder.() -> Unit): DropTableBuilder.() -> Unit = init

    /** Registreert een drop-table voor één of meerdere NPC-IDs. */
    fun register(
        table: DropTableBuilder.() -> Unit,
        vararg ids: Int,
        type: DropTableType = DropTableType.KILL
    ) {
        ids.forEach { tables[type]!![it] = table }
    }

    /**
     * Roept de drop af bij het sterven van een NPC.
     * We klonen de tile hier direct, zodat latere mutaties niet doorsijpelen.
     */
    fun getDrop(
        world: World,
        player: Player,
        npcId: Int,
        tile: Tile,
        type: DropTableType = DropTableType.KILL
    ) {
        try {
            val dropTile = Tile(tile.x, tile.z, tile.height)
            getDrop(player, npcId, type)
                ?.forEach { createDrop(world, it, dropTile, player) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Maakt alleen de lijst met Item-objecten aan.
     */
    fun getDrop(
        player: Player,
        tableId: Int,
        type: DropTableType = DropTableType.KILL
    ): MutableList<Item>? {
        val items = mutableListOf<Item>()
        val bldr = tables[type]!![tableId] ?: return null

        try {
            val table = DropTableBuilder(player, prng).apply(bldr)
            val subtables = table.tables.values.toList()

            // eerst guaranteed
            subtables
                .firstOrNull { it.name == GUARANTEED_TABLE_NAME }
                ?.entries
                ?.mapNotNull { (it.drop as? DropEntry.ItemDrop)?.item }
                ?.let { items.addAll(it) }

            // daarna één item per andere tabel
            subtables
                .filterNot { it.name == GUARANTEED_TABLE_NAME }
                .map { DropEntry.TableDrop(it) }
                .flatMap { it.getDrop() }
                .let { items.addAll(it) }

            return items
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * Probeer in je inventory te stoppen, anders op de grond.
     */
    fun createDropInventory(
        player: Player,
        tableId: Int,
        type: DropTableType = DropTableType.KILL
    ): MutableList<Item>? {
        return try {
            val drops = getDrop(player, tableId, type)
            drops?.forEach { item ->
                if (
                    player.inventory.hasFreeSpace() ||
                    (player.inventory.contains(item.id)
                            && item.getDef(player.world.definitions).stackable)
                ) {
                    player.inventory.add(item)
                } else {
                    val groundItem = GroundItem(item.id, item.amount, player.tile, player)
                    player.world.spawn(groundItem)
                }
            }
            drops
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun hasInventorySpaceForAnyDrop(
        player: Player,
        tableId: Int,
        type: DropTableType
    ): Boolean? {
        val bldr = tables[type]!![tableId] ?: return null
        return try {
            val table = DropTableBuilder(player, prng).apply(bldr)
            val totalRequired = table.tables.values.sumOf {
                requiredInventorySpacesToReceiveDrop(player, it)
            }
            totalRequired <= player.inventory.freeSlotCount
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun requiredInventorySpacesToReceiveDrop(
        player: Player,
        table: DropTable
    ): Int {
        if (table.name == GUARANTEED_TABLE_NAME) {
            return table.entries
                .map { it.drop }
                .filterIsInstance<DropEntry.ItemDrop>()
                .map { it.item.id }
                .count(player.inventory::requiresFreeSlotToAdd)
        }

        for (entry in table.entries) {
            val needed = when (val drop = entry.drop) {
                is DropEntry.NothingDrop -> 0

                // **Aangepast**: gebruik count in plaats van sumOf om int-ambiguïteit te voorkomen
                is DropEntry.MultiDrop -> drop.items.count {
                    player.inventory.requiresFreeSlotToAdd(it.id)
                }

                is DropEntry.ItemRangeDrop ->
                    if (player.inventory.requiresFreeSlotToAdd(drop.item.id)) 1 else 0

                is DropEntry.ItemDrop ->
                    if (player.inventory.requiresFreeSlotToAdd(drop.item.id)) 1 else 0

                is DropEntry.TableDrop ->
                    requiredInventorySpacesToReceiveDrop(player, drop.table)

                else -> 0
            }
            if (needed > 0) return needed
        }
        return 0
    }

    /**
     * Recursieve drop uit geneste tables. Met een default-`else` om 'exhaustive' te maken.
     */
    private fun DropEntry.TableDrop.getDrop(): List<Item> {
        val entries = table.entries
        val idx = prng.nextInt(entries.last().index)
        val drop = entries.first { it.index > idx }.drop
        return when (drop) {
            is DropEntry.NothingDrop   -> emptyList()
            is DropEntry.ItemDrop      -> listOf(drop.item)
            is DropEntry.MultiDrop     -> drop.getDrop()
            is DropEntry.TableDrop     -> drop.getDrop()
            is DropEntry.ItemRangeDrop -> listOf(drop.getDrop())
            else                       -> emptyList()
        }
    }

    /**
     * Lange levende veiligheid: klonen we de tile nogmaals vlak voor spawn.
     */
    private fun createDrop(
        world: World,
        item: Item,
        tile: Tile,
        owner: Pawn
    ) {
        val safeTile = Tile(tile.x, tile.z, tile.height)
        val ground = GroundItem(item.id, item.amount, safeTile, owner as Player)
        world.spawn(ground)
    }
}

@DslMarker
private annotation class BuilderDslMarker

@BuilderDslMarker
class DropTableBuilder(
    val player: Player,
    private val prng: SecureRandom
) {
    val tables = hashMapOf<String, DropTable>()

    fun guaranteed(builder: TableBuilder.() -> Unit) = table(GUARANTEED_TABLE_NAME, builder)
    fun main(builder: TableBuilder.() -> Unit)       = table("main", builder)

    fun table(name: String, builder: TableBuilder.() -> Unit) {
        val built = TableBuilder(player, prng, name).apply(builder).build()
        tables[name.lowercase()] = built
    }

    internal fun build(): List<DropTable> = tables.values.toList()
}

@BuilderDslMarker
class TableBuilder(
    private val player: Player,
    private val prng: SecureRandom,
    val name: String
) {
    private var totalSlots = if (name == GUARANTEED_TABLE_NAME) 128 else 0
    private var occupiedSlots = 0
    private val entries = mutableListOf<Entry>()

    fun total(total: Int) { totalSlots = total }

    fun obj(id: Int, quantity: Int = 1, slots: Int = 1) {
        val item = Item(id, quantity)
        occupiedSlots += slots
        entries.add(Entry(occupiedSlots, DropEntry.ItemDrop(item)))
    }

    fun obj(id: Int, quantityRange: IntRange, slots: Int = 1) {
        val item = Item(id, quantityRange.first)
        occupiedSlots += slots
        entries.add(Entry(occupiedSlots, DropEntry.ItemRangeDrop(item, quantityRange)))
    }

    fun objs(vararg items: Item, slots: Int = 1) {
        occupiedSlots += slots
        entries.add(Entry(occupiedSlots, DropEntry.MultiDrop(*items)))
    }

    fun table(builder: DropTableBuilder.() -> Unit, slots: Int = 1) {
        val sub = DropTableBuilder(player, prng).apply(builder).build().first()
        occupiedSlots += slots
        entries.add(Entry(occupiedSlots, DropEntry.TableDrop(sub)))
    }

    fun nothing(slots: Int) {
        occupiedSlots += slots
        entries.add(Entry(occupiedSlots, DropEntry.NothingDrop))
    }

    internal fun build(): DropTable = DropTable(name, entries.toTypedArray())

    data class Entry(
        val index: Int,
        val drop: DropEntry
    )
}
