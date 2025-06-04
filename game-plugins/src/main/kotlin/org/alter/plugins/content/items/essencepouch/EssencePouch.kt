package org.alter.plugins.content.items.other.essencepouch

/**
 * @author Triston Plummer ("Dread')
 *
 * @param id        The essence pouch item id
 * @param levelReq  The Runecrafting level required to use the pouch
 * @param capacity  The maximum capacity of the essence pouch
 */
data class EssencePouch(val id: Int, val levelReq: Int, val capacity: Int)