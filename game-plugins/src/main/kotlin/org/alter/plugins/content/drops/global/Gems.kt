//package org.alter.plugins.content.drops.global
//
//import org.alter.api.EquipmentType
//import org.alter.api.cfg.Items
//import org.alter.plugins.content.drops.DropTableFactory
//object Gems {
//    val gemTable =
//        DropTableFactory.build {
//            main {
//
//                //val hasRingOfWealth = player.equipment.hasAt(EquipmentType.RING.id, Items.RING_OF_WEALTH)
//
//                total(10240)
//                if (!hasRingOfWealth) nothing(512)
//                obj(Items.UNCUT_OPAL, quantity = 1, slots = if (hasRingOfWealth) 160 else 80)
//                obj(Items.UNCUT_JADE, quantity = 1, slots = if (hasRingOfWealth) 160 else 80)
//                obj(Items.UNCUT_RED_TOPAZ, quantity = 1, slots = if (hasRingOfWealth) 160 else 80)
//                obj(Items.UNCUT_SAPPHIRE, quantity = 1, slots = if (hasRingOfWealth) 80 else 40)
//                obj(Items.UNCUT_EMERALD, quantity = 1, slots = if (hasRingOfWealth) 80 else 40)
//                obj(Items.UNCUT_RUBY, quantity = 1, slots = if (hasRingOfWealth) 40 else 20)
//                obj(Items.UNCUT_DIAMOND, quantity = 1, slots = if (hasRingOfWealth) 40 else 20)
//                obj(Items.UNCUT_DRAGONSTONE, quantity = 1, slots = if (hasRingOfWealth) 4 else 2)
//                obj(Items.UNCUT_ONYX, quantity = 1, slots = if (hasRingOfWealth) 4 else 2)
//                obj(Items.UNCUT_ZENYTE, quantity = 1, slots = if (hasRingOfWealth) 2 else 1)
//            }
//        }
//}
