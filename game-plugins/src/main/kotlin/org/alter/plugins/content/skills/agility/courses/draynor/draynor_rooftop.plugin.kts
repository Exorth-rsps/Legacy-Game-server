package org.alter.plugins.content.skills.agility.courses.draynor

import org.alter.api.cfg.Animation
import org.alter.api.cfg.Items
import org.alter.game.model.Tile
import org.alter.plugins.content.skills.agility.AgilityCourseRegistry
import org.alter.plugins.content.skills.agility.agilityCourse

private val draynorRooftopCourse = agilityCourse("draynor_rooftop") {
    name = "Draynor Village Rooftop Course"
    minimumLevel = 10
    totalLapExperience = 120.0
    regions(12338)
    description = "The introductory rooftop course that circles Draynor Village. Each lap yields roughly 120 Agility experience."

    obstacle(
        name = "Rough wall",
        experience = 8.0,
        description = "Climb the rough wall north of the bank to access the rooftops.",
        startTile = Tile(3103, 3261, 0),
        animation = Animation.AGILITY_CLIMB_UP,
        animationDuration = 4,
    )
    obstacle(
        name = "First tightrope",
        experience = 10.0,
        description = "Cross the first tightrope to the neighbouring building.",
        startTile = Tile(3103, 3261, 3),
        animation = Animation.AGILITY_LOG_WALK,
        animationDuration = 7,
    )
    obstacle(
        name = "Second tightrope",
        experience = 10.0,
        description = "Traverse the second tightrope heading east.",
        startTile = Tile(3108, 3263, 3),
        animation = Animation.AGILITY_LOG_WALK,
        animationDuration = 6,
    )
    obstacle(
        name = "Narrow wall",
        experience = 7.5,
        description = "Balance along the narrow wall overlooking the marketplace.",
        startTile = Tile(3110, 3265, 3),
        animation = Animation.AGILITY_CROSS_LEDGE_RIGHT,
        animationDuration = 5,
    )
    obstacle(
        name = "Wall jump",
        experience = 8.0,
        description = "Vault the crumbling wall to the southern rooftop.",
        startTile = Tile(3113, 3265, 3),
        animation = Animation.AGILITY_JUMP,
        animationDuration = 5,
    )
    obstacle(
        name = "First gap",
        experience = 8.0,
        description = "Leap across the gap towards the manor.",
        startTile = Tile(3116, 3263, 3),
        animation = Animation.AGILITY_JUMP,
        animationDuration = 4,
    )
    obstacle(
        name = "Second gap",
        experience = 8.0,
        description = "Hop the second gap towards the final building.",
        startTile = Tile(3120, 3262, 3),
        animation = Animation.AGILITY_JUMP,
        animationDuration = 4,
    )
    obstacle(
        name = "Crate dismount",
        experience = 18.0,
        description = "Climb down the stacked crates back to street level.",
        startTile = Tile(3122, 3262, 3),
        animation = Animation.AGILITY_CLIMB_DOWN,
        animationDuration = 3,
    )

    reward(
        item = Items.MARK_OF_GRACE,
        averagePerLap = 0.22,
        averagePerHour = 13.0,
        notes = "Marks of grace spawn roughly once every 4-5 laps on average."
    )
}

AgilityCourseRegistry.register(draynorRooftopCourse)
