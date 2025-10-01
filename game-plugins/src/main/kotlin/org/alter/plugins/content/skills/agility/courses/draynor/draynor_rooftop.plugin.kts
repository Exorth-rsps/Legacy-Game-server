package org.alter.plugins.content.skills.agility.courses.draynor

import org.alter.api.cfg.Animation
import org.alter.api.cfg.Items
import org.alter.api.cfg.Objs
import org.alter.api.ext.getInteractingGameObj
import org.alter.api.ext.message
import org.alter.game.model.Tile
import org.alter.plugins.content.skills.agility.AgilityCourseRegistry
import org.alter.plugins.content.skills.agility.AgilityObstacle
import org.alter.plugins.content.skills.agility.agilityCourse

private val draynorRooftopCourse = agilityCourse("draynor_rooftop") {
    name = "Draynor Village Rooftop Course"
    minimumLevel = 10
    totalLapExperience = 120.0
    description = "The introductory rooftop course that circles Draynor Village. Each lap yields roughly 120 Agility experience."

    obstacle(
        name = "Rough wall",
        experience = 8.0,
        description = "Climb the rough wall north of the bank to access the rooftops.",
        objectIds = setOf(Objs.ROUGH_WALL),
        objectTile = Tile(3103, 3261, 0),
        startTile = Tile(3103, 3261, 0),
        endTile = Tile(3103, 3261, 3),
        animation = Animation.AGILITY_CLIMB_UP,
        animationDuration = 4,
        interactionOption = "Climb",
    )
    obstacle(
        name = "First tightrope",
        experience = 10.0,
        description = "Cross the first tightrope to the neighbouring building.",
        objectIds = setOf(Objs.TIGHTROPE),
        objectTile = Tile(3103, 3261, 3),
        startTile = Tile(3103, 3261, 3),
        endTile = Tile(3108, 3263, 3),
        animation = Animation.AGILITY_LOG_WALK,
        animationDuration = 7,
        interactionOption = "Cross",
    )
    obstacle(
        name = "Second tightrope",
        experience = 10.0,
        description = "Traverse the second tightrope heading east.",
        objectIds = setOf(Objs.TIGHTROPE_11406),
        objectTile = Tile(3108, 3263, 3),
        startTile = Tile(3108, 3263, 3),
        endTile = Tile(3110, 3265, 3),
        animation = Animation.AGILITY_LOG_WALK,
        animationDuration = 6,
        interactionOption = "Cross",
    )
    obstacle(
        name = "Narrow wall",
        experience = 7.5,
        description = "Balance along the narrow wall overlooking the marketplace.",
        objectIds = setOf(Objs.NARROW_WALL),
        objectTile = Tile(3110, 3265, 3),
        startTile = Tile(3110, 3265, 3),
        endTile = Tile(3113, 3265, 3),
        animation = Animation.AGILITY_CROSS_LEDGE_RIGHT,
        animationDuration = 5,
        interactionOption = "Balance",
    )
    obstacle(
        name = "Wall jump",
        experience = 8.0,
        description = "Vault the crumbling wall to the southern rooftop.",
        objectIds = setOf(Objs.WALL_11630),
        objectTile = Tile(3113, 3265, 3),
        startTile = Tile(3113, 3265, 3),
        endTile = Tile(3116, 3263, 3),
        animation = Animation.AGILITY_JUMP,
        animationDuration = 5,
        interactionOption = "Jump",
    )
    obstacle(
        name = "First gap",
        experience = 8.0,
        description = "Leap across the gap towards the manor.",
        objectIds = setOf(Objs.GAP_10861),
        objectTile = Tile(3116, 3263, 3),
        startTile = Tile(3116, 3263, 3),
        endTile = Tile(3120, 3262, 3),
        animation = Animation.AGILITY_JUMP,
        animationDuration = 4,
        interactionOption = "Jump",
    )
    obstacle(
        name = "Second gap",
        experience = 8.0,
        description = "Hop the second gap towards the final building.",
        objectIds = setOf(Objs.GAP_10862),
        objectTile = Tile(3120, 3262, 3),
        startTile = Tile(3120, 3262, 3),
        endTile = Tile(3122, 3262, 3),
        animation = Animation.AGILITY_JUMP,
        animationDuration = 4,
        interactionOption = "Jump",
    )
    obstacle(
        name = "Crate dismount",
        experience = 18.0,
        description = "Climb down the stacked crates back to street level.",
        objectIds = setOf(Objs.CRATE_11632),
        objectTile = Tile(3122, 3262, 3),
        startTile = Tile(3122, 3262, 3),
        endTile = Tile(3122, 3261, 0),
        animation = Animation.AGILITY_CLIMB_DOWN,
        animationDuration = 3,
        interactionOption = "Climb-down",
    )

    reward(
        item = Items.MARK_OF_GRACE,
        averagePerLap = 0.22,
        averagePerHour = 13.0,
        notes = "Marks of grace spawn roughly once every 4-5 laps on average."
    )
}

AgilityCourseRegistry.register(draynorRooftopCourse)

private val draynorObstaclesByObject: Map<Int, List<AgilityObstacle>> =
    draynorRooftopCourse.obstacles
        .filter { it.interactionOption != null && it.objectIds.isNotEmpty() }
        .flatMap { obstacle -> obstacle.objectIds.map { it to obstacle } }
        .groupBy({ it.first }, { it.second })

draynorObstaclesByObject.forEach { (objectId, obstacles) ->
    val option = obstacles.mapNotNull { it.interactionOption }.toSet()
    if (option.size != 1) {
        return@forEach
    }

    on_obj_option(obj = objectId, option = option.first()) {
        val obj = player.getInteractingGameObj()
        if (obj == null) {
            player.message("Nothing interesting happens.")
            return@on_obj_option
        }

        val obstacle = chooseDraynorObstacle(obstacles, obj.tile)
        if (obstacle == null) {
            player.message("Nothing interesting happens.")
            return@on_obj_option
        }

        player.queue {
            val startTile = obstacle.startTile
            if (startTile != null && player.tile != startTile) {
                player.moveTo(startTile)
                wait(1)
            }

            obstacle.animation?.let { animationId ->
                player.animate(animationId)
                val duration = obstacle.animationDuration ?: 0
                if (duration > 0) {
                    wait(duration)
                }
            }

            if (obstacle.animation == null) {
                wait(1)
            }

            val endTile = obstacle.endTile
            if (endTile != null && player.tile != endTile) {
                player.moveTo(endTile)
            }

            player.message("You attempt the ${obstacle.name.lowercase()}.")
        }
    }
}

private fun chooseDraynorObstacle(obstacles: List<AgilityObstacle>, objectTile: Tile): AgilityObstacle? {
    if (obstacles.size == 1) {
        return obstacles.first()
    }

    return obstacles.firstOrNull { it.objectTile == objectTile }
        ?: obstacles.firstOrNull { it.startTile == objectTile }
        ?: obstacles.firstOrNull()
}
