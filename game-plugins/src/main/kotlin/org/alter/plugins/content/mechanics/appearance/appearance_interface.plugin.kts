package org.alter.plugins.content.mechanics.appearance

import org.alter.game.model.appearance.*
import org.alter.game.model.attr.APPEARANCE_SET_ATTR
import org.alter.game.sync.block.UpdateBlockType
import org.alter.plugins.content.mechanics.appearance.Appearance_interface_plugin.AppearanceOps.Companion.isColourOp
import org.alter.plugins.content.mechanics.appearance.Appearance_interface_plugin.AppearanceOps.Companion.isLookOp

val APPEARANCE_INTERFACE_ID = 679

/**
 * Change [Gender] to [Gender.MALE]
 */
on_button(APPEARANCE_INTERFACE_ID, 65) {
    player.setVarbit(11697, 0)
    player.appearance = Appearance.DEFAULT_MALE
    player.addBlock(UpdateBlockType.APPEARANCE)
}

/**
 * Change [Gender] to [Gender.FEMALE]
 */
on_button(APPEARANCE_INTERFACE_ID, 66) {
    player.setVarbit(11697, 1)
    player.appearance = Appearance.DEFAULT_FEMALE
    player.addBlock(UpdateBlockType.APPEARANCE)
}

/**
 * Confirms [Player] [Appearance] selection and closes interface
 */
on_button(APPEARANCE_INTERFACE_ID, 68) {
    player.attr[APPEARANCE_SET_ATTR] = true
    player.unlock()
    player.closeInterface(APPEARANCE_INTERFACE_ID)
}

enum class AppearanceOps(val component: Int) {
    HEAD(10),
    JAW(14),
    TORSO(18),
    ARMS(22),
    HANDS(26),
    LEGS(30),
    FEET(34),
    HAIR_COLOUR(41),
    TORSO_COLOUR(45),
    LEGS_COLOUR(49),
    FEET_COLOUR(53),
    SKIN_COLOUR(57),
    NONE(0);

    companion object {
        fun isValidOp(option: Int): Boolean = (option in 10..60)
        fun isLookOp(option: Int): Boolean = (option in 10..37)
        fun isColourOp(option: Int): Boolean = (option in 41..60)
    }
}

AppearanceOps.values()
    .filterNot { it == AppearanceOps.NONE }
    .forEach { op ->

        // **-- DECREMENT LOOK/COLOUR --**
        on_button(APPEARANCE_INTERFACE_ID, op.component + 2) {
            val opt = op.component

            when {
                // LOOK OPTIES
                isLookOp(opt) -> {
                    val pos = (opt - 10) / 4

                    // Vrouwelijke spelers mogen optie 1 (JAW) niet aanpassen
                    if (player.appearance.gender == Gender.FEMALE && pos == 1) {
                        return@on_button
                    }

                    // Bepaal de lijst met beschikbare looks voor dit onderdeel
                    val looks = getLooks(opt, player.appearance.gender)

                    // Huidige index in player.appearance.looks
                    val current = if (player.appearance.gender == Gender.MALE) {
                        player.appearance.looks[pos]
                    } else {
                        // FEMALE: slot 0 = head, verder slot-1
                        if (pos == 0) player.appearance.looks[0]
                        else player.appearance.looks[pos - 1]
                    }

                    // Wrap-around voor decrement
                    val previous = if (current - 1 < 0) looks.size - 1 else current - 1

                    // Schrijf terug naar de juiste positie
                    if (player.appearance.gender == Gender.MALE) {
                        player.appearance.looks[pos] = previous
                    } else {
                        if (pos == 0) {
                            player.appearance.looks[0] = previous
                        } else {
                            player.appearance.looks[pos - 1] = previous
                        }
                    }

                    player.addBlock(UpdateBlockType.APPEARANCE)
                }

                // KLEUR OPTIES
                isColourOp(opt) -> {
                    val pos = (opt - 41) / 4
                    val current = player.appearance.colors[pos]
                    val colors = getColours(opt)
                    val previous = if (current - 1 < 0) colors.size - 1 else current - 1
                    player.appearance.colors[pos] = previous
                    player.addBlock(UpdateBlockType.APPEARANCE)
                }
            }
        }

        // **-- INCREMENT LOOK/COLOUR --**
        on_button(APPEARANCE_INTERFACE_ID, op.component + 3) {
            val opt = op.component

            when {
                // LOOK OPTIES
                isLookOp(opt) -> {
                    val pos = (opt - 10) / 4

                    // Vrouwelijke spelers mogen optie 1 (JAW) niet aanpassen
                    if (player.appearance.gender == Gender.FEMALE && pos == 1) {
                        return@on_button
                    }

                    val looks = getLooks(opt, player.appearance.gender)

                    val current = if (player.appearance.gender == Gender.MALE) {
                        player.appearance.looks[pos]
                    } else {
                        if (pos == 0) player.appearance.looks[0]
                        else player.appearance.looks[pos - 1]
                    }

                    // Wrap-around voor increment (let op >=)
                    val next = if (current + 1 >= looks.size) 0 else current + 1

                    if (player.appearance.gender == Gender.MALE) {
                        player.appearance.looks[pos] = next
                    } else {
                        if (pos == 0) {
                            player.appearance.looks[0] = next
                        } else {
                            player.appearance.looks[pos - 1] = next
                        }
                    }

                    player.addBlock(UpdateBlockType.APPEARANCE)
                }

                // KLEUR OPTIES
                isColourOp(opt) -> {
                    val pos = (opt - 41) / 4
                    val current = player.appearance.colors[pos]
                    val colors = getColours(opt)
                    val next = if (current + 1 >= colors.size) 0 else current + 1
                    player.appearance.colors[pos] = next
                    player.addBlock(UpdateBlockType.APPEARANCE)
                }
            }
        }
    }

/**
 * Haalt het juiste look-array op voor de gegeven optie en gender
 */
fun getLooks(option: Int, gender: Gender): Array<Int> =
    when (gender) {
        Gender.MALE -> when ((option - 10) / 4) {
            0 -> Looks.getHeads(gender)
            1 -> Looks.getJaws(gender)
            2 -> Looks.getTorsos(gender)
            3 -> Looks.getArms(gender)
            4 -> Looks.getHands(gender)
            5 -> Looks.getLegs(gender)
            6 -> Looks.getFeets(gender)
            else -> arrayOf(-1)
        }
        Gender.FEMALE -> when ((option - 10) / 4) {
            0 -> Looks.getHeads(gender)
            2 -> Looks.getTorsos(gender)
            3 -> Looks.getArms(gender)
            4 -> Looks.getHands(gender)
            5 -> Looks.getLegs(gender)
            6 -> Looks.getFeets(gender)
            else -> arrayOf(-1)
        }
    }

/**
 * Haalt het juiste colour-array op voor de gegeven optie
 */
fun getColours(option: Int): Array<Int> =
    when ((option - 41) / 4) {
        0 -> Colours.HAIR_COLOURS
        1 -> Colours.TORSO_COLOURS
        2 -> Colours.LEG_COLOURS
        3 -> Colours.FEET_COLOURS
        4 -> Colours.SKIN_COLOURS
        else -> arrayOf(-1)
    }
