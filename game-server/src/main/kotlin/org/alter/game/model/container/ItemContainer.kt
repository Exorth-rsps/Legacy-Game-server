package org.alter.game.model.container

import org.alter.game.fs.DefinitionSet
import org.alter.game.fs.def.ItemDef
import org.alter.game.model.attr.OTHER_ITEM_SLOT_ATTR
import org.alter.game.model.container.key.ContainerKey
import org.alter.game.model.item.Item
import org.alter.game.model.item.SlotItem

import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * An [ItemContainer] represents a collection of ordered [Item]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ItemContainer(val definitions: DefinitionSet, val key: ContainerKey) : Iterable<Item?> {

    constructor(definitions: DefinitionSet, capacity: Int, stackType: ContainerStackType)
            : this(definitions, ContainerKey("", capacity, stackType))

    constructor(other: ItemContainer) : this(other.definitions, other.capacity, other.stackType) {
        for (i in 0 until capacity) {
            val item = if (other[i] != null) Item(other[i]!!) else null
            set(i, item)
        }
    }

    /** The total amount of items that the container can hold at a time. */
    val capacity = key.capacity

    private val stackType = key.stackType
    private val items = Array<Item?>(capacity) { null }

    /** A flag which indicates that the [items] has been modified since the last game cycle. */
    var dirty = true

    override fun iterator(): Iterator<Item?> = items.iterator()

    /** The raw array backing this container. */
    val rawItems: Array<Item?> = items

    fun contains(item: Int): Boolean = items.any { it?.id == item }

    fun containsAny(item: Int, vararg others: Int): Boolean =
        items.any { it != null && (it.id == item || it.id in others) }

    fun hasAt(slot: Int, itemId: Int): Boolean = items[slot]?.id == itemId

    /** The first free slot, or -1 if none. */
    val nextFreeSlot: Int get() = items.indexOfFirst { it == null }

    val freeSlotCount: Int get() = items.count { it == null }

    fun getLastFreeSlot(): Int {
        var lastEmpty = -1
        for (index in items.indices) {
            if (items[index] == null) lastEmpty = index
            else break
        }
        return lastEmpty
    }

    fun getLastFreeSlot(startIndex: Int): Int {
        var lastEmpty = -1
        items.indices.reversed().forEach {
            if (it > startIndex && items[it] == null) lastEmpty = it
        }
        return lastEmpty
    }

    fun getLastFreeSlotReversed(): Int {
        var lastEmpty = -1
        for (index in items.indices.reversed()) {
            if (items[index] == null) lastEmpty = index
            else break
        }
        return lastEmpty
    }

    val occupiedSlotCount: Int get() = items.count { it != null }
    val isFull: Boolean get() = items.all { it != null }
    val isEmpty: Boolean get() = items.none { it != null }
    val hasAny: Boolean get() = items.any { it != null }
    val hasSpace: Boolean get() = nextFreeSlot != -1

    fun hasFreeSpace(): Boolean = freeSlotCount > 0

    fun requiresFreeSlotToAdd(item: Int): Boolean {
        val def = definitions.get(ItemDef::class.java, item)
        return if (def.stackable) !contains(item) else true
    }

    fun getItemCount(itemId: Int): Int {
        var amount: Long = 0
        for (item in items) {
            if (item?.id == itemId) amount += item.amount
        }
        return Math.min(amount, Int.MAX_VALUE.toLong()).toInt()
    }

    fun getItemIndex(itemId: Int, skipAttrItems: Boolean): Int {
        for (i in 0 until capacity) {
            val it = items[i]
            if (it?.id == itemId && (!skipAttrItems || !it.hasAnyAttr())) return i
        }
        return -1
    }

    fun toMap(): Map<Int, Item> =
        items.mapIndexedNotNull { i, it -> it?.let { i to it } }.toMap()

    fun removeAll() {
        for (i in 0 until capacity) set(i, null)
    }

    fun add(item: Int, amount: Int = 1, assureFullInsertion: Boolean = true,
            forceNoStack: Boolean = false, beginSlot: Int = -1): ItemTransaction {
        val def = definitions.get(ItemDef::class.java, item)
        val stack = !forceNoStack &&
                stackType != ContainerStackType.NO_STACK &&
                (def.stackable || stackType == ContainerStackType.STACK)
        val previousAmount = if (stack) getItemCount(item) else 0

        if (previousAmount == Int.MAX_VALUE) return ItemTransaction(amount, 0, emptyList())

        val freeSlotCount = freeSlotCount
        if (freeSlotCount == 0 && (!stack || previousAmount == 0)) {
            return ItemTransaction(amount, 0, emptyList())
        }

        if (assureFullInsertion) {
            if (stack && previousAmount > Int.MAX_VALUE - amount) {
                return ItemTransaction(amount, 0, emptyList())
            }
            if (!stack && amount > freeSlotCount) {
                return ItemTransaction(amount, 0, emptyList())
            }
        } else {
            if (stack && previousAmount == Int.MAX_VALUE) {
                return ItemTransaction(amount, 0, emptyList())
            } else if (!stack && freeSlotCount == 0) {
                return ItemTransaction(amount, 0, emptyList())
            }
        }

        var completed = 0
        val added = mutableListOf<SlotItem>()

        if (!stack) {
            val start = Math.max(0, beginSlot)
            for (i in start until capacity) {
                if (items[i] == null) {
                    val add = Item(item)
                    set(i, add)
                    added.add(SlotItem(i, add))
                    if (++completed >= amount) break
                }
            }
        } else {
            var stackIndex = getItemIndex(item, skipAttrItems = true)
            if (stackIndex == -1) {
                stackIndex = if (beginSlot == -1) nextFreeSlot
                else (beginSlot until capacity).firstOrNull { items[it] == null } ?: -1
                if (stackIndex == -1) {
                    logger.error(RuntimeException("Unable to find a free slot for a stackable item. [capacity=$capacity, item=$item, quantity=$amount]")) {}
                    return ItemTransaction(amount, completed, emptyList())
                }
            }

            val stackAmount = get(stackIndex)?.amount ?: 0
            val total = Math.min(Int.MAX_VALUE.toLong(), stackAmount.toLong() + amount).toInt()
            val add = Item(item, total)
            set(stackIndex, add)
            added.add(SlotItem(stackIndex, add))
            completed = total - stackAmount
        }
        return ItemTransaction(amount, completed, added)
    }

    fun add(item: Item, assureFullInsertion: Boolean = true,
            forceNoStack: Boolean = false, beginSlot: Int = -1): ItemTransaction =
        add(item.id, item.amount, assureFullInsertion, forceNoStack, beginSlot)

    fun remove(item: Int, amount: Int = 1, assureFullRemoval: Boolean = false,
               beginSlot: Int = -1): ItemTransaction {
        val hasAmount = getItemCount(item)
        if (assureFullRemoval && hasAmount < amount) return ItemTransaction(amount, 0, emptyList())
        if (!assureFullRemoval && hasAmount < 1)     return ItemTransaction(amount, 0, emptyList())

        var totalRemoved = 0
        val removed = mutableListOf<SlotItem>()
        val skipped = if (beginSlot != -1) 0 until beginSlot else null
        val start = if (beginSlot != -1) beginSlot else 0

        for (i in start until capacity) {
            val cur = items[i] ?: continue
            if (cur.id == item) {
                val toRemove = Math.min(cur.amount, amount - totalRemoved)
                totalRemoved += toRemove
                cur.amount -= toRemove
                if (cur.amount == 0) {
                    val rem = Item(items[i]!!)
                    items[i] = null
                    removed.add(SlotItem(i, rem))
                }
                if (totalRemoved == amount) break
            }
        }

        if (skipped != null && totalRemoved < amount) {
            for (i in skipped) {
                val cur = items[i] ?: continue
                if (cur.id == item) {
                    val toRemove = Math.min(cur.amount, amount - totalRemoved)
                    totalRemoved += toRemove
                    cur.amount -= toRemove
                    if (cur.amount == 0) {
                        val rem = Item(items[i]!!)
                        items[i] = null
                        removed.add(SlotItem(i, rem))
                    }
                    if (totalRemoved == amount) break
                }
            }
        }

        if (totalRemoved > 0) dirty = true
        return ItemTransaction(amount, totalRemoved, removed)
    }

    fun remove(item: Item, assureFullRemoval: Boolean = false,
               beginSlot: Int = -1): ItemTransaction =
        remove(item.id, item.amount, assureFullRemoval, beginSlot)

    fun replace(remove: Int, add: Int, slot: Int = -1): Boolean =
        if (remove(remove, beginSlot = slot).hasSucceeded())
            add(add, beginSlot = slot).hasSucceeded()
        else false

    fun replaceWithItemRequirement(remove: Int, add: Int, required: Int, slot: Int = -1): Boolean =
        if (contains(required)) replace(remove, add, slot) else false

    fun replaceAndRemoveAnother(remove: Int, add: Int, other: Item,
                                slot: Int = -1, otherSlot: Int = -1): Boolean {
        val taken = remove(other, assureFullRemoval = true, beginSlot = otherSlot).hasSucceeded()
        return if (replace(remove, add, slot) && taken) true
        else if (taken) {
            add(other, assureFullInsertion = true, beginSlot = otherSlot)
            false
        } else false
    }

    fun replaceAndRemoveAnotherWithItemRequirement(remove: Int, add: Int,
                                                   other: Item, required: Int,
                                                   slot: Int = -1, otherSlot: Int = -1): Boolean =
        if (contains(required)) replaceAndRemoveAnother(remove, add, other, slot, otherSlot) else false

    fun replaceBoth(removeItem: Int, addItem: Int,
                    otherItem: Int, otherAddItem: Int,
                    slot: Int = -1, otherSlot: Int = -1): Boolean {
        val taken = replace(otherItem, otherAddItem, otherSlot)
        return if (replace(removeItem, addItem, slot) && taken) true
        else if (taken) {
            replace(otherAddItem, otherItem, otherSlot)
            false
        } else false
    }

    fun swap(from: Int, to: Int) {
        val copy = items[from]
        set(from, items[to])
        set(to, copy)
    }

    fun shift() {
        val newItems = Array<Item?>(capacity) { null }
        var idx = 0
        for (i in 0 until capacity) {
            val it = this[i] ?: continue
            newItems[idx++] = it
        }
        removeAll()
        for (i in 0 until capacity) set(i, newItems[i])
    }

    // ---- Safe get/set to avoid ArrayIndexOutOfBounds ----

    operator fun get(index: Int): Item? {
        return if (index in items.indices) {
            items[index]
        } else {
            logger.warn { "Invalid get(): slot index $index out of bounds (0..${capacity - 1})" }
            null
        }
    }

    operator fun set(index: Int, item: Item?) {
        if (index in items.indices) {
            items[index] = item
            dirty = true
        } else {
            logger.warn { "Invalid set(): slot index $index out of bounds (0..${capacity - 1}), item=$item" }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
