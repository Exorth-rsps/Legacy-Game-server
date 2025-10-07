package org.alter.plugins.content.skills.agility.courses.draynor

import org.alter.api.Skills
import org.alter.api.cfg.Animation
import org.alter.api.cfg.Items
import org.alter.api.cfg.Objs
import org.alter.api.ext.getInteractingGameObj
import org.alter.api.ext.message
import org.alter.game.model.Direction
import org.alter.game.model.ForcedMovement
import org.alter.game.model.LockState
import org.alter.game.model.MovementQueue
import org.alter.game.model.Tile
import org.alter.game.model.entity.Player
import org.alter.game.model.queue.QueueTask
import org.alter.plugins.content.skills.agility.AgilityCourseRegistry
import org.alter.plugins.content.skills.agility.AgilityObstacle
import org.alter.plugins.content.skills.agility.AgilityObstacleMovement
import org.alter.plugins.content.skills.agility.agilityCourse
import kotlin.math.abs
import kotlin.math.max

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
        objectTile = Tile(3103, 3279, 0),
        startTile = Tile(3103, 3279, 0),
        endTile = Tile(3102, 3279, 3),
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
        movementType = AgilityObstacleMovement.STEP,
        movementStepDuration = 1,
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
        movementType = AgilityObstacleMovement.STEP,
        movementStepDuration = 1,
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
        movementType = AgilityObstacleMovement.STEP,
        movementStepDuration = 1,
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
        interactionOption = "Jump-up",
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
        interactionOption = "Cross",
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
        interactionOption = "Cross",
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

private val draynorFinalObstacle: AgilityObstacle? = draynorRooftopCourse.obstacles.lastOrNull()
private val draynorLapBonusExperience: Double = (draynorRooftopCourse.totalLapExperience - draynorRooftopCourse.obstacles
    .sumOf { it.experience ?: 0.0 }).coerceAtLeast(0.0)

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

            player.message(obstacle.description ?: "You attempt the ${obstacle.name.lowercase()}.")

            val animationId = obstacle.animation
            val animationDuration = obstacle.animationDuration
            if (animationId != null) {
                player.animate(animationId)
            }

            val movementTicks = when (obstacle.movementType) {
                AgilityObstacleMovement.TELEPORT -> performTeleportMovement(player, obstacle)
                AgilityObstacleMovement.FORCED -> performForcedMovement(player, obstacle, animationDuration)
                AgilityObstacleMovement.STEP -> performStepMovement(player, obstacle)
            }

            if (animationId != null) {
                val targetDuration = animationDuration ?: movementTicks
                val remaining = targetDuration - movementTicks
                when {
                    remaining > 0 -> wait(remaining)
                    targetDuration == 0 && movementTicks == 0 -> wait(DEFAULT_ANIMATION_FALLOFF_TICKS)
                }
                player.animate(-1)
            } else if (movementTicks == 0) {
                wait(1)
            }

            obstacle.experience?.let { xp ->
                player.addXp(Skills.AGILITY, xp)
            }

            if (draynorFinalObstacle != null && obstacle == draynorFinalObstacle) {
                if (draynorLapBonusExperience > 0) {
                    player.addXp(Skills.AGILITY, draynorLapBonusExperience)
                }

                val formattedXp = formatExperience(draynorRooftopCourse.totalLapExperience)
                player.message("You complete the ${draynorRooftopCourse.name} and earn $formattedXp Agility experience.")
                draynorRooftopCourse.description?.let { player.message(it) }
            } else {
                player.message("You successfully traverse the ${obstacle.name.lowercase()}.")
            }
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

private suspend fun QueueTask.performTeleportMovement(player: Player, obstacle: AgilityObstacle): Int {
    val endTile = obstacle.endTile
    if (endTile != null && !player.tile.sameAs(endTile)) {
        player.moveTo(endTile)
    }
    return 0
}

private suspend fun QueueTask.performForcedMovement(
    player: Player,
    obstacle: AgilityObstacle,
    animationDuration: Int?
): Int {
    val startTile = obstacle.startTile ?: return performTeleportMovement(player, obstacle)
    val endTile = obstacle.endTile ?: return performTeleportMovement(player, obstacle)
    val ticks = calculateMovementTicks(startTile, endTile, animationDuration, obstacle.movementStepDuration)
    val movement = createForcedMovement(startTile, endTile, ticks)
    player.forceMove(this, movement, cycleDuration = ticks)
    if (!player.tile.sameAs(endTile)) {
        player.moveTo(endTile)
    }
    return ticks
}

private suspend fun QueueTask.performStepMovement(player: Player, obstacle: AgilityObstacle): Int {
    val endTile = obstacle.endTile ?: return 0
    val startTile = obstacle.startTile ?: player.tile
    val stepPath = buildStepPath(startTile, endTile)
    if (stepPath.isEmpty()) {
        if (!player.tile.sameAs(endTile)) {
            player.moveTo(endTile)
        }
        return 0
    }

    val perTileTicks = (obstacle.movementStepDuration ?: DEFAULT_STEP_DURATION_TICKS).coerceAtLeast(1)
    var totalTicks = 0

    player.movementQueue.clear()

    stepPath.forEach { stepTile ->
        player.movementQueue.addStep(stepTile, MovementQueue.StepType.FORCED_WALK, detectCollision = false)

        var stepTicks = 0
        while (!player.tile.sameAs(stepTile)) {
            if (!player.movementQueue.hasDestination()) {
                player.moveTo(stepTile)
                break
            }

            wait(1)
            stepTicks++
        }

        while (stepTicks < perTileTicks) {
            wait(1)
            stepTicks++
        }

        totalTicks += stepTicks
    }

    if (!player.tile.sameAs(endTile)) {
        player.moveTo(endTile)
    }

    player.movementQueue.clear()

    return totalTicks
}

private fun buildStepPath(startTile: Tile, endTile: Tile): List<Tile> {
    val steps = calculateStepCount(startTile, endTile)
    if (steps == 0) {
        return emptyList()
    }

    val path = mutableListOf<Tile>()
    val stepX = sign(endTile.x - startTile.x)
    val stepZ = sign(endTile.z - startTile.z)
    var currentX = startTile.x
    var currentZ = startTile.z

    repeat(steps) { index ->
        if (currentX != endTile.x) {
            currentX += stepX
        }
        if (currentZ != endTile.z) {
            currentZ += stepZ
        }

        val height = if (index == steps - 1) endTile.height else startTile.height
        path += Tile(currentX, currentZ, height)
    }

    return path
}

private fun calculateMovementTicks(
    startTile: Tile,
    endTile: Tile,
    animationDuration: Int?,
    movementStepDuration: Int?
): Int {
    val steps = calculateStepCount(startTile, endTile)
    if (steps == 0) {
        return (animationDuration ?: 0).coerceAtLeast(1)
    }
    val perTileTicks = (movementStepDuration ?: DEFAULT_STEP_DURATION_TICKS).coerceAtLeast(1)
    val stepTicks = perTileTicks * steps
    val requested = animationDuration ?: stepTicks
    return max(stepTicks, requested)
}

private fun calculateStepCount(startTile: Tile, endTile: Tile): Int {
    val dx = abs(endTile.x - startTile.x)
    val dz = abs(endTile.z - startTile.z)
    return max(dx, dz)
}

private fun createForcedMovement(startTile: Tile, endTile: Tile, durationTicks: Int): ForcedMovement {
    val ticks = durationTicks.coerceAtLeast(1)
    val duration = ticks * 30
    val directionAngle = directionAngleBetween(startTile, endTile)
    return ForcedMovement.of(startTile, endTile, duration, duration, directionAngle, LockState.FULL)
}

private fun directionAngleBetween(startTile: Tile, endTile: Tile): Int {
    val deltaX = endTile.x - startTile.x
    val deltaZ = endTile.z - startTile.z

    return when {
        deltaX == 0 && deltaZ > 0 -> Direction.NORTH.angle
        deltaX == 0 && deltaZ < 0 -> Direction.SOUTH.angle
        deltaZ == 0 && deltaX > 0 -> Direction.EAST.angle
        deltaZ == 0 && deltaX < 0 -> Direction.WEST.angle
        deltaX > 0 && deltaZ > 0 -> Direction.NORTH_EAST.angle
        deltaX > 0 && deltaZ < 0 -> Direction.SOUTH_EAST.angle
        deltaX < 0 && deltaZ > 0 -> Direction.NORTH_WEST.angle
        deltaX < 0 && deltaZ < 0 -> Direction.SOUTH_WEST.angle
        else -> Direction.NORTH.angle
    }
}

private const val DEFAULT_STEP_DURATION_TICKS = 1
private const val DEFAULT_ANIMATION_FALLOFF_TICKS = 1

private fun formatExperience(xp: Double): String {
    return if (xp % 1.0 == 0.0) {
        xp.toInt().toString()
    } else {
        "%.1f".format(xp)
    }
}

private fun sign(value: Int): Int = when {
    value > 0 -> 1
    value < 0 -> -1
    else -> 0
}
