package org.alter.plugins.content.skills.crafting

import org.alter.api.cfg.Items
import org.alter.plugins.content.skills.crafting.data.Gems
import org.alter.plugins.content.skills.crafting.data.Spin

val spinningObjs = setOf(
    Objs.SPINNING_WHEEL_25824,
    Objs.SPINNING_WHEEL_8748
)
val spinningObj = 25824

Gems.values().forEach { gems ->
    on_item_on_item(Items.CHISEL, gems.uncutGem) {
        player.queue { Cutting.gemCut(this, gems) }
    }
}
spinningObjs.forEach { spinningObj ->
    Spin.values().forEach { spin ->
        on_item_on_obj(obj = spinningObj, item = spin.unSpun) {
            player.queue { Spinning.Spin(this, spin) }
        }
    }
}
