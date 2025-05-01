package org.alter.game.model.appearance

import org.alter.game.model.appearance.Looks.getArms
import org.alter.game.model.appearance.Looks.getFeets
import org.alter.game.model.appearance.Looks.getHands
import org.alter.game.model.appearance.Looks.getHeads
import org.alter.game.model.appearance.Looks.getJaws
import org.alter.game.model.appearance.Looks.getLegs
import org.alter.game.model.appearance.Looks.getTorsos

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Appearance(val looks: IntArray, val colors: IntArray, var gender: Gender) {
    var renderAnim = -1

    /**
     * Definieer de onderdelen die we ondersteunen (HEAD t/m FEET).
     */
    private enum class Part(val slotIndex: Int) {
        HEAD(0),
        JAW(1),
        TORSO(2),
        ARMS(3),
        HANDS(4),
        LEGS(5),
        FEET(6)
    }

    /**
     * Haal de model-ID op van het gekozen onderdeel, of -1 bij een ongeldige index.
     */
    fun getLook(option: Int): Int {
        // Bepaal welk model-array en welke slotIndex we nodig hebben
        val (models, slot) = when (gender) {
            Gender.MALE -> when (option) {
                Part.HEAD.slotIndex  -> getHeads(gender)  to Part.HEAD.slotIndex
                Part.JAW.slotIndex   -> getJaws(gender)   to Part.JAW.slotIndex
                Part.TORSO.slotIndex -> getTorsos(gender) to Part.TORSO.slotIndex
                Part.ARMS.slotIndex  -> getArms(gender)   to Part.ARMS.slotIndex
                Part.HANDS.slotIndex -> getHands(gender)  to Part.HANDS.slotIndex
                Part.LEGS.slotIndex  -> getLegs(gender)   to Part.LEGS.slotIndex
                Part.FEET.slotIndex  -> getFeets(gender)  to Part.FEET.slotIndex
                else -> return -1
            }
            Gender.FEMALE -> when (option) {
                Part.HEAD.slotIndex  -> getHeads(gender)  to Part.HEAD.slotIndex
                Part.TORSO.slotIndex -> getTorsos(gender) to Part.JAW.slotIndex    // torso staat in looks[1]
                Part.ARMS.slotIndex  -> getArms(gender)   to Part.TORSO.slotIndex // arms in looks[2]
                Part.HANDS.slotIndex -> getHands(gender)  to Part.ARMS.slotIndex  // hands in looks[3]
                Part.LEGS.slotIndex  -> getLegs(gender)   to Part.HANDS.slotIndex // legs in looks[4]
                Part.FEET.slotIndex  -> getFeets(gender)  to Part.LEGS.slotIndex  // feet in looks[5]
                else -> return -1
            }
        }

        // 1) Veilig het gekozen model-slot lezen
        val lookIndex = looks.getOrNull(slot) ?: return -1

        // 2) Veilig de master-array indexeren
        return models.getOrNull(lookIndex) ?: -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Appearance

        if (!looks.contentEquals(other.looks)) return false
        if (!colors.contentEquals(other.colors)) return false
        if (gender != other.gender) return false

        return true
    }

    override fun hashCode(): Int {
        var result = looks.contentHashCode()
        result = 31 * result + colors.contentHashCode()
        result = 31 * result + gender.hashCode()
        return result
    }

    companion object {
        private val DEFAULT_COLORS = intArrayOf(0, 27, 9, 0, 0)

        private val DEFAULT_MALE_LOOKS = intArrayOf(15, 9, 3, 8, 0, 3, 1)
        val DEFAULT_MALE = Appearance(DEFAULT_MALE_LOOKS, DEFAULT_COLORS, Gender.MALE)

        private val DEFAULT_FEMALE_LOOKS = intArrayOf(0, 0, 0, 0, 0, 0)
        val DEFAULT_FEMALE = Appearance(DEFAULT_FEMALE_LOOKS, DEFAULT_COLORS, Gender.FEMALE)
    }
}
