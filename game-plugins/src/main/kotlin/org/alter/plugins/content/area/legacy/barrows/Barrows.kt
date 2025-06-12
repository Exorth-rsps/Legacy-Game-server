package org.alter.plugins.content.area.legacy.barrows

import org.alter.game.model.attr.AttributeKey
import org.alter.game.model.Tile
import org.alter.api.cfg.Objs

object Barrows {
    // bit flags for each brother in order: Dharok, Verac, Ahrim, Guthan, Karil, Torag
    val PROGRESS_ATTR = AttributeKey<Int>(persistenceKey = "barrows_progress")
    val LAST_BROTHER_ATTR = AttributeKey<Int>(persistenceKey = "barrows_last")

    data class Brother(val id: Int, val mound: Tile, val crypt: Tile)

    val BROTHERS = listOf(
        Brother(org.alter.api.cfg.Npcs.DHAROK_THE_WRETCHED, Tile(3575, 3297), Tile(3556, 9718, 3)), //20720
        Brother(org.alter.api.cfg.Npcs.VERAC_THE_DEFILED, Tile(3556, 3297), Tile(3578, 9706, 3)), //20772
        Brother(org.alter.api.cfg.Npcs.AHRIM_THE_BLIGHTED, Tile(3565, 3288), Tile(3557, 9703, 3)), //20770
        Brother(org.alter.api.cfg.Npcs.GUTHAN_THE_INFESTED, Tile(3578, 3282), Tile(3534, 9704, 3)), //20722
        Brother(org.alter.api.cfg.Npcs.KARIL_THE_TAINTED, Tile(3565, 3275), Tile(3546, 9684, 3)), //20771
        Brother(org.alter.api.cfg.Npcs.TORAG_THE_CORRUPTED, Tile(3553, 3282), Tile(3568, 9683, 3)), //20721
    )

    const val CHEST = 20723
    const val TUNNEL_INDEX = 5

    val SARCOPHAGUS_IDS = intArrayOf(
        Objs.SARCOPHAGUS_20720,
        Objs.SARCOPHAGUS_20772,
        Objs.SARCOPHAGUS_20770,
        Objs.SARCOPHAGUS_20722,
        Objs.SARCOPHAGUS_20771,
        Objs.SARCOPHAGUS_20721
    )
}
