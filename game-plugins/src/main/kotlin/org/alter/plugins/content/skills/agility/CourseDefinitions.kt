package org.alter.plugins.content.skills.agility

import org.alter.game.model.Tile

/**
 * Represents a single interactive obstacle inside of an agility course.
 *
 * [objectIds] can be used once we start wiring the course logic so that
 * each configured entry can be mapped to the corresponding world object.
 *
 * [objectTile], [startTile], and [endTile] describe the tile of the interactive
 * object, the tile a player should step onto before starting the obstacle and
 * the tile they should land on once the interaction has finished respectively.
 * The optional [animation] and [animationDuration] (in game ticks) document the
 * expected movement and timing, while [interactionOption] stores the object
 * option that should trigger the obstacle.
 */
data class AgilityObstacle(
    val name: String,
    val experience: Double? = null,
    val description: String? = null,
    val objectIds: Set<Int> = emptySet(),
    val objectTile: Tile? = null,
    val startTile: Tile? = null,
    val endTile: Tile? = null,
    val animation: Int? = null,
    val animationDuration: Int? = null,
    val interactionOption: String? = null,
)

/**
 * Describes a lap-based reward. Rooftop courses, for instance, spawn marks
 * of grace at a predictable long term average which we capture through the
 * [averagePerLap] and [averagePerHour] fields.
 */
data class AgilityReward(
    val item: Int,
    val averagePerLap: Double? = null,
    val averagePerHour: Double? = null,
    val notes: String? = null
)

/**
 * An agility course combines a level requirement, the total experience per
 * lap and rich metadata about its constituent obstacles and rewards.
 */
data class AgilityCourse(
    val id: String,
    val name: String,
    val minimumLevel: Int,
    val totalLapExperience: Double,
    val obstacles: List<AgilityObstacle>,
    val rewards: List<AgilityReward>,
    val description: String? = null
)

/**
 * Registry that keeps track of all configured agility courses. While there is
 * currently no logic that consumes it, having the central data structure in
 * place makes it trivial to plug it into future gameplay plugins.
 */
object AgilityCourseRegistry {
    private val courses = linkedMapOf<String, AgilityCourse>()

    fun register(course: AgilityCourse) {
        require(course.id.isNotBlank()) { "Course id cannot be blank" }
        courses[course.id] = course
    }

    fun get(id: String): AgilityCourse? = courses[id]

    fun all(): Collection<AgilityCourse> = courses.values
}

class AgilityCourseBuilder(private val id: String) {
    var name: String = id
    var minimumLevel: Int = 1
    var totalLapExperience: Double? = null
    var description: String? = null
    private val obstacles = mutableListOf<AgilityObstacle>()
    private val rewards = mutableListOf<AgilityReward>()

    fun obstacle(
        name: String,
        experience: Double? = null,
        description: String? = null,
        objectIds: Set<Int> = emptySet(),
        objectTile: Tile? = null,
        startTile: Tile? = null,
        endTile: Tile? = null,
        animation: Int? = null,
        animationDuration: Int? = null,
        interactionOption: String? = null,
    ) {
        obstacles += AgilityObstacle(
            name = name,
            experience = experience,
            description = description,
            objectIds = objectIds,
            objectTile = objectTile,
            startTile = startTile,
            endTile = endTile,
            animation = animation,
            animationDuration = animationDuration,
            interactionOption = interactionOption,
        )
    }

    fun reward(
        item: Int,
        averagePerLap: Double? = null,
        averagePerHour: Double? = null,
        notes: String? = null
    ) {
        rewards += AgilityReward(item, averagePerLap, averagePerHour, notes)
    }

    internal fun build(): AgilityCourse {
        val computedLapExperience = totalLapExperience ?: obstacles
            .mapNotNull(AgilityObstacle::experience)
            .sum()
        require(computedLapExperience >= 0) { "Total lap experience must be non-negative" }
        return AgilityCourse(
            id = id,
            name = name,
            minimumLevel = minimumLevel,
            totalLapExperience = computedLapExperience,
            obstacles = obstacles.toList(),
            rewards = rewards.toList(),
            description = description
        )
    }
}

fun agilityCourse(id: String, block: AgilityCourseBuilder.() -> Unit): AgilityCourse {
    val builder = AgilityCourseBuilder(id)
    builder.block()
    return builder.build()
}
