package org.alter.plugins.content.skills.prayer

import org.alter.game.fs.def.ItemDef
import org.alter.game.model.entity.Player
import org.alter.api.Skills
import org.alter.api.cfg.Animation
import org.alter.api.ext.message

/**
 * @author Fritz <frikkipafi@gmail.com>
 */

    object Offer {

        // Controleer of een speler botten kan aanbieden
        fun canOffer(p: Player, bones: Bones): Boolean {
            // Controleer bijvoorbeeld of de speler de botten in hun inventaris heeft
            return p.inventory.contains(bones.id)
        }

        // Altar = 0 (gilded), 1 (ecto), 2 (chaos), 3 (normal)
        fun OfferBones(p: Player, bones: Bones, Altar: Int) {
            // Valideer of de speler botten kan aanbieden
            if (!canOffer(p, bones)) {
                p.message("You can't offer these bones.")
                return
            }

            // Verkrijg de naam van de botten en de bijbehorende ervaringswaarden
            val boneName = p.world.definitions.get(ItemDef::class.java, bones.id).name
            val altars = arrayOf(bones.gilded, bones.ecto, bones.chaos, bones.normal)

            // Controleer of het altaar geldig is
            if (Altar !in 0..3) {
                p.message("Invalid altar selection.")
                return
            }

            // Wachtproces voor het aanbieden van botten
            p.queue {
                p.lock()
                p.inventory.remove(item = bones.id)
                p.addXp(Skills.PRAYER, altars[Altar]) // Gebruik correcte index
                p.animate(Animation.OFFER_BONES_TO_ALTER_ANIM)
                p.resetFacePawn()
                wait(3) // Tijd voor de animatie
                p.unlock()
            }

            // Geef een bericht aan de speler
            p.message("You offer the ${boneName.lowercase()} to the altar.")
        }
    }
