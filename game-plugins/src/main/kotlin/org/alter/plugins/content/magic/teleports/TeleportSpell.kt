package org.alter.plugins.content.magic.teleports

import org.alter.game.model.Area
import org.alter.plugins.content.magic.TeleportType

enum class TeleportSpell(val spellName: String, val type: TeleportType, val endArea: Area,
                         val xp: Double, val paramItem: Int? = null) {
    /**
     * Standard.
     */

    LUMBRIDGE("Lumbridge Home Teleport", TeleportType.MODERN, Area(2963, 3377, 2968, 3380), 0.0),
    ENTRANA("Entrana Teleport", TeleportType.MODERN, Area(2825, 3343, 2828, 3345), 0.0),
    DRAYNOR("Draynor Teleport", TeleportType.MODERN, Area(3077, 3248, 3082, 3252), 0.0),
    FISHING_GUILD("Fishing Guild Teleport", TeleportType.MODERN, Area(2602, 3398, 2606, 3402), 0.0),
    //LEGENDS_GUILD("Legends Guild Teleport", TeleportType.MODERN, Area(2728, 3351, 2729, 3359), 0.0),
    //SHILO("Shilo Village Teleport", TeleportType.MODERN, Area(2845, 2962, 2850, 2966), 0.0),
    //EDGEVILLE("Edgeville Teleport", TeleportType.MODERN, Area(3084, 3484, 3096, 3487), 0.0),
    //GNOME_STRONGHOLD("Gnome Stronghold Teleport", TeleportType.MODERN, Area(2459, 3423, 2462, 3430), 0.0),
   // VARROCK("Varrock Teleport", TeleportType.MODERN, Area(3209, 3422, 3216, 3424), 0.0),
 //   KOUREND_CASTLE("Kourend Castle Teleport", TeleportType.MODERN, Area(1633, 3665, 1639, 3670), 0.0),

//
//    /**
//     * Ancients.
//     */
    EDGEVILLE("Edgeville Home Teleport", TeleportType.ANCIENT, Area(2963, 3377, 2968, 3380), 0.0),
//    PADDEWWA("Paddewwa Teleport", TeleportType.ANCIENT, Area(3095, 9880, 3099, 9884), 64.0),
//    SENNTISTEN("Senntisten Teleport", TeleportType.ANCIENT, Area(3346, 3343, 3350, 3346), 70.0),
//    KHARYRLL("Kharyrll Teleport", TeleportType.ANCIENT, Area(3491, 3476, 3494, 3478), 76.0),
//    LASSAR("Lassar Teleport", TeleportType.ANCIENT, Area(3003, 3473, 3008, 3476), 82.0),
//    DAREEYAK("Dareeyak Teleport", TeleportType.ANCIENT, Area(2965, 3693, 2969, 3697), 88.0),
//    CARRALLANGAR("Carrallangar Teleport", TeleportType.ANCIENT, Area(3146, 3668, 3149, 3671), 94.0),
//    ANNAKARL("Annakarl Teleport", TeleportType.ANCIENT, Area(3293, 3885, 3297, 3888), 100.0),
//    GHORROCK("Ghorrock Teleport", TeleportType.ANCIENT, Area(2966, 3872, 2972, 3878), 106.0),
//
//    /**
//     * Lunars.
//     */
//    MOONCLAN("Moonclan Teleport", TeleportType.LUNAR, Area(2096, 3912, 2099, 3915), 66.0),
//    OURANIA("Ourania Teleport", TeleportType.LUNAR, Area(2454, 3232, 2455, 3233), 69.0),
//    WATERBIRTH("Waterbirth Teleport", TeleportType.LUNAR, Area(2545, 3756, 2548, 3759), 71.0),
//    BARBARIAN("Barbarian Teleport", TeleportType.LUNAR, Area(2547, 3566, 2549, 3571), 76.0),
//    KHAZARD("Khazard Teleport", TeleportType.LUNAR, Area(2652, 3156, 2660, 3159), 80.0),
//    CATHERBY("Catherby Teleport", TeleportType.LUNAR, Area(2802, 3432, 2806, 3435), 92.0),
//    ICE_PLATEAU("Ice Plateau Teleport", TeleportType.LUNAR, Area(2979, 3940, 2984, 3946), 96.0),
//
//    /**
//     * Arceuus.
//     */
//    ARCEUUS_LIBRARY("Arceuus Library Teleport", TeleportType.ARCEUUS, Area(1631, 3835, 1635, 3838), 9.0),
//    DRAYNOR_MANOR("Draynor Manor Teleport", TeleportType.ARCEUUS, Area(3107, 3327, 3113, 3330), 16.0),
//    BATTLEFRONT("Battlefront Teleport", TeleportType.ARCEUUS, Area(1342, 3682, 1346, 3685), 19.0),
//    MIND_ALTAR("Mind Altar Teleport", TeleportType.ARCEUUS, Area(2979, 3509, 2980, 3512), 22.0),
//    SALVE_GRAVEYARD("Salve Graveyard Teleport", TeleportType.ARCEUUS, Area(3437, 3467, 3442, 3471), 30.0),
//    FENKENSTRAIN_CASTLE("Fenkenstrain's Castle Teleport", TeleportType.ARCEUUS, Area(3546, 3527, 3549, 3529), 50.0),
//    WEST_ARDOUGNE("West Ardougne Teleport", TeleportType.ARCEUUS, Area(2528, 3304, 2534, 3308), 68.0),
//    HARMONY_ISLAND("Harmony Island Teleport", TeleportType.ARCEUUS, Area(3793, 2857, 3801, 2863), 74.0),
//    CEMETERY("Cemetery Teleport", TeleportType.ARCEUUS, Area(2964, 3760, 2969, 3766), 82.0),
//    BARROWS("Barrows Teleport", TeleportType.ARCEUUS, Area(3563, 3312, 3566, 3315), 90.0),
//    APE_ATOLL_DUNGEON("Ape Atoll Teleport", TeleportType.ARCEUUS, Area(2764, 9102, 2767, 9104), 100.0, paramItem = 20427)
    ;

    companion object {
        val values = enumValues<TeleportSpell>()
    }
}