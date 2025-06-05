package org.alter.plugins.content.area.legacy.barrows

import org.alter.game.model.attr.AttributeKey
import org.alter.game.model.Tile

object Barrows {
    // bit flags for each brother in order: Dharok, Verac, Ahrim, Guthan, Karil, Torag
    val PROGRESS_ATTR = AttributeKey<Int>(persistenceKey = "barrows_progress")

    data class Brother(val id: Int, val mound: Tile, val crypt: Tile)

    val BROTHERS = listOf(
        Brother(org.alter.api.cfg.Npcs.DHAROK_THE_WRETCHED, Tile(3575, 3297), Tile(3575, 9703, 3)),
        Brother(org.alter.api.cfg.Npcs.VERAC_THE_DEFILED, Tile(3556, 3297), Tile(3556, 9701, 3)),
        Brother(org.alter.api.cfg.Npcs.AHRIM_THE_BLIGHTED, Tile(3565, 3288), Tile(3565, 9708, 3)),
        Brother(org.alter.api.cfg.Npcs.GUTHAN_THE_INFESTED, Tile(3575, 3282), Tile(3575, 9694, 3)),
        Brother(org.alter.api.cfg.Npcs.KARIL_THE_TAINTED, Tile(3565, 3275), Tile(3565, 9683, 3)),
        Brother(org.alter.api.cfg.Npcs.TORAG_THE_CORRUPTED, Tile(3553, 3282), Tile(3553, 9689, 3)),
    )

    const val CHEST = 20973
}
