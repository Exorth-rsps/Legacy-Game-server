package org.alter.game.model.container

import java.nio.file.Paths

import net.runelite.cache.fs.Store

import org.alter.game.fs.DefinitionSet
import org.alter.game.fs.def.ItemDef
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemContainerTests {

    @Test
    fun createContainer() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)

        assertEquals(container.capacity, CAPACITY)
        assertEquals(container.nextFreeSlot, 0)
        assertEquals(container.freeSlotCount, CAPACITY)
        assertEquals(container.occupiedSlotCount, 0)
    }

    @Test
    fun addNoted() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val result = container.add(item = 4152, amount = 3, forceNoStack = false, assureFullInsertion = true, beginSlot = 0)

        assertTrue(result.hasSucceeded())
        assertEquals(container.occupiedSlotCount, 1)
        assertEquals(container.nextFreeSlot, 1)
        assertTrue(container[0] != null)
        assertEquals(container[0]!!.amount, 3)
    }

    @Test
    fun addUnnoted() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val result = container.add(item = 4151, amount = 3, forceNoStack = false, assureFullInsertion = true, beginSlot = 0)

        assertTrue(result.hasSucceeded())
        assertEquals(container.occupiedSlotCount, 3)
        assertEquals(container.nextFreeSlot, 3)
        assertNotNull(container[0])
        assertEquals(container[0]!!.id, 4151)
        assertEquals(container[0]!!.amount, 1)
        assertNotNull(container[1])
        assertEquals(container[1]!!.id, 4151)
        assertEquals(container[1]!!.amount, 1)
        assertNotNull(container[2])
        assertEquals(container[2]!!.id, 4151)
        assertEquals(container[2]!!.amount, 1)
        assertNull(container[3])
    }

    @Test
    fun addNotedNoStack() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val result = container.add(item = 4152, amount = 3, forceNoStack = true, assureFullInsertion = true, beginSlot = 0)

        assertTrue(result.hasSucceeded())
        assertEquals(container.occupiedSlotCount, 3)
        assertEquals(container.nextFreeSlot, 3)
        assertNotNull(container[0])
        assertEquals(container[0]!!.id, 4152)
        assertEquals(container[0]!!.amount, 1)
        assertNotNull(container[1])
        assertEquals(container[1]!!.id, 4152)
        assertEquals(container[1]!!.amount, 1)
        assertNotNull(container[2])
        assertEquals(container[2]!!.id, 4152)
        assertEquals(container[2]!!.amount, 1)
        assertNull(container[3])
    }

    @Test
    fun addOneTooManyStrict() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val result = container.add(item = 4151, amount = CAPACITY + 1, forceNoStack = false, assureFullInsertion = true, beginSlot = 0)
        assertFalse(result.hasSucceeded())
        assertEquals(container.nextFreeSlot, 0)
    }

    @Test
    fun addOneTooManyLoose() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val loose = container.add(item = 4151, amount = CAPACITY + 1, forceNoStack = false, assureFullInsertion = false, beginSlot = 0)
        assertFalse(loose.hasSucceeded())
        assertEquals(container.freeSlotCount, 0)
    }

    @Test
    fun addOverflowAmount() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)

        container.add(item = 4152, amount = Int.MAX_VALUE, forceNoStack = false, assureFullInsertion = true, beginSlot = 0)
        assertEquals(container.getItemCount(4152), Int.MAX_VALUE)

        val result = container.add(item = 4152, amount = 1, forceNoStack = false, assureFullInsertion = true, beginSlot = 0)
        assertFalse(result.hasSucceeded())
        assertEquals(result.getLeftOver(), 1)
        assertEquals(container.getItemCount(4152), Int.MAX_VALUE)
    }

    @Test
    fun addToMiddle() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        val result = container.add(item = 4151, amount = 1, forceNoStack = false, assureFullInsertion = false, beginSlot = CAPACITY / 2)
        assertTrue(result.hasSucceeded())
        assertEquals(container.freeSlotCount, CAPACITY - 1)
        assertEquals(container.nextFreeSlot, 0)
        assertNotNull(container[CAPACITY / 2])
        assertEquals(container[CAPACITY / 2]!!.id, 4151)
    }

    @Test
    fun addToStackContainer() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.STACK)
        val result = container.add(item = 4151, amount = Int.MAX_VALUE, forceNoStack = false, assureFullInsertion = false, beginSlot = 0)

        assertTrue(result.hasSucceeded())
        assertNotNull(container[0])
        assertEquals(container[0]!!.amount, Int.MAX_VALUE)
    }

    @Test
    fun removeStackableAssureFullRemoval() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        container.add(item = 4152, amount = 3)

        val tx = container.remove(item = 4152, amount = 3, assureFullRemoval = true)

        assertTrue(tx.hasSucceeded())
        assertEquals(3, tx.completed)
        assertEquals(CAPACITY, container.freeSlotCount)
        assertEquals(0, container.occupiedSlotCount)
        assertNull(container[0])
    }

    @Test
    fun removeStackableAssureFullRemovalFail() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        container.add(item = 4152, amount = 2)

        val tx = container.remove(item = 4152, amount = 3, assureFullRemoval = true)

        assertFalse(tx.hasSucceeded())
        assertEquals(0, tx.completed)
        assertEquals(27, container.freeSlotCount)
        assertEquals(1, container.occupiedSlotCount)
        assertNotNull(container[0])
        assertEquals(2, container[0]!!.amount)
    }

    @Test
    fun removeStackableLoosePartial() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        container.add(item = 4152, amount = 2)

        val tx = container.remove(item = 4152, amount = 3, assureFullRemoval = false)

        assertFalse(tx.hasSucceeded())
        assertEquals(2, tx.completed)
        assertEquals(CAPACITY, container.freeSlotCount)
        assertEquals(0, container.occupiedSlotCount)
        assertNull(container[0])
    }

    @Test
    fun removeUnstackableAssureFullRemoval() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        container.add(item = 4151, amount = 3)

        val tx = container.remove(item = 4151, amount = 2, assureFullRemoval = true)

        assertTrue(tx.hasSucceeded())
        assertEquals(2, tx.completed)
        assertEquals(27, container.freeSlotCount)
        assertEquals(1, container.occupiedSlotCount)
        assertNull(container[0])
        assertNull(container[1])
        assertNotNull(container[2])
    }

    @Test
    fun removeUnstackableAssureFullRemovalFail() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        container.add(item = 4151, amount = 1)

        val tx = container.remove(item = 4151, amount = 2, assureFullRemoval = true)

        assertFalse(tx.hasSucceeded())
        assertEquals(0, tx.completed)
        assertEquals(27, container.freeSlotCount)
        assertEquals(1, container.occupiedSlotCount)
        assertNotNull(container[0])
    }

    @Test
    fun removeUnstackableLoosePartial() {
        val container = ItemContainer(definitions, CAPACITY, ContainerStackType.NORMAL)
        container.add(item = 4151, amount = 2)

        val tx = container.remove(item = 4151, amount = 3, assureFullRemoval = false)

        assertFalse(tx.hasSucceeded())
        assertEquals(2, tx.completed)
        assertEquals(CAPACITY, container.freeSlotCount)
        assertEquals(0, container.occupiedSlotCount)
        assertNull(container[0])
        assertNull(container[1])
    }

    companion object {

        private const val CAPACITY = 28

        private val definitions = DefinitionSet()

        private lateinit var store: Store

        @BeforeClass
        @JvmStatic
        fun loadCache() {
            store = Store(Paths.get("..", "data", "cache").toFile())
            store.load()

            definitions.loadAll(store)

            assertNotEquals(definitions.getCount(ItemDef::class.java), 0)
        }
    }
}
