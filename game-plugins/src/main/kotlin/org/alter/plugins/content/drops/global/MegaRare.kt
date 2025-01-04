package org.alter.plugins.content.drops.global

import org.alter.api.EquipmentType
import org.alter.api.cfg.Items
import org.alter.plugins.content.drops.DropTableFactory

/**
 * @author Alycia <https://github.com/alycii>
 */
object MegaRare {
    val megaRareTable =
        DropTableFactory.build {
            main {

                // TODO: check all 4 rings
                val hasRingOfWealth = player.equipment.hasAt(EquipmentType.RING.id, Items.RING_OF_WEALTH)

                total(1024)
                if (!hasRingOfWealth) nothing(904)
                obj(Items.RUNE_SPEAR, quantity = 1, slots = if (hasRingOfWealth) 545 else 64)
                obj(Items.SHIELD_LEFT_HALF, quantity = 1, slots = if (hasRingOfWealth) 273 else 32)
                obj(Items.DRAGON_SPEAR, quantity = 1, slots = if (hasRingOfWealth) 206 else 24)
            }
        }
}
