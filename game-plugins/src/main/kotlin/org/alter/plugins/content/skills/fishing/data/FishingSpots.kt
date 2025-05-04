package org.alter.plugins.content.skills.fishing.data

import org.alter.api.cfg.Animation
import org.alter.api.cfg.Items
import org.alter.api.cfg.Npcs
import org.alter.api.cfg.Objs

/**
 * @author Fritz <frikkipafi@gmail.com>
 */

val SMALL_NET = arrayOf(Npcs.FISHING_SPOT_3317, Npcs.FISHING_SPOT_1530)
val BIG_NET = arrayOf(Npcs.FISHING_SPOT_1511, Npcs.FISHING_SPOT_1520)
val FISHING_ROD1 = arrayOf(Npcs.FISHING_SPOT_1530, Npcs.ROD_FISHING_SPOT_1527, Npcs.ROD_FISHING_SPOT_1526)
val FLYFISHING_ROD = arrayOf(Npcs.ROD_FISHING_SPOT_1527, Npcs.ROD_FISHING_SPOT_1526)

//fishing spot 1523

enum class FishingSpots(val spotEntityId: Int, val option: String, val toolId: Int, val baitId: Int, val animation: Int, vararg fish: Fish) {
    TUTORIAL_NET(Npcs.FISHING_SPOT_3317, "net", Items.SMALL_FISHING_NET, -1, Animation.FISHING_NET, Fish.SHRIMP),
    NET(Npcs.FISHING_SPOT_1530, "net", Items.SMALL_FISHING_NET, -1, Animation.FISHING_NET, Fish.SHRIMP, Fish.ANCHOVIES),
    BAIT(Npcs.FISHING_SPOT_1530, "bait", Items.FISHING_ROD, Items.FISHING_BAIT, Animation.FISHING_ROD, Fish.SARDINE, Fish.HERRING),
    BAIT2(Npcs.ROD_FISHING_SPOT_1527, "bait", Items.FISHING_ROD, Items.FISHING_BAIT, Animation.FISHING_ROD, Fish.PIKE),
    BAIT3(Npcs.ROD_FISHING_SPOT_1526, "bait", Items.FISHING_ROD, Items.FISHING_BAIT, Animation.FISHING_ROD, Fish.PIKE),
    LURE(Npcs.ROD_FISHING_SPOT_1527, "lure", Items.FLY_FISHING_ROD, Items.FEATHER, Animation.FISHING_ROD, Fish.TROUT, Fish.SALMON),
    LURE2(Npcs.ROD_FISHING_SPOT_1526, "lure", Items.FLY_FISHING_ROD, Items.FEATHER, Animation.FISHING_ROD, Fish.TROUT, Fish.SALMON, Fish.RAINBOWFISH),
    BIG_NET(Npcs.FISHING_SPOT_1511, "net", Items.BIG_FISHING_NET, -1, Animation.FISHING_NET, Fish.MACKEREL, Fish.COD, Fish.BASS),
    BIG_NET2(Npcs.FISHING_SPOT_1520, "big net", Items.BIG_FISHING_NET, -1, Animation.FISHING_NET, Fish.MACKEREL, Fish.COD, Fish.BASS),
    CAGE(Npcs.FISHING_SPOT_1522, "cage", Items.LOBSTER_POT, -1, Animation.FISHING_LOBSTER_POT, Fish.LOBSTER),
    HARPOON(Npcs.FISHING_SPOT_1522, "harpoon", Items.HARPOON, -1, Animation.FISHING_HARPOON, Fish.TUNA, Fish.SWORDFISH),
    HARPOON2(Npcs.FISHING_SPOT_5821, "harpoon", Items.HARPOON, -1, Animation.FISHING_HARPOON, Fish.SHARK),
    HARPOON3(Npcs.FISHING_SPOT_1511, "harpoon", Items.HARPOON, -1, Animation.FISHING_HARPOON, Fish.SHARK),
    NET2(Npcs.FISHING_SPOT_5821, "net", Items.SMALL_FISHING_NET, -1, Animation.FISHING_NET, Fish.MONKFISH);
    private val fish = fish

    fun getFish(): Array<out Fish> { return this.fish
    }
}