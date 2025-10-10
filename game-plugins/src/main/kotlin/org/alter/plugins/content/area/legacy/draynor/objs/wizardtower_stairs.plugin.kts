package org.alter.plugins.content.area.legacy.draynor.objs

val stairs =
    arrayOf(
        Objs.STAIRCASE_12536,
        Objs.STAIRCASE_12537,
        Objs.STAIRCASE_12538,
    )

stairs.forEach { stairs ->
    val name =
        world.definitions
            .get(ObjectDef::class.java, stairs)
            .name
            .lowercase()
    if (if_obj_has_option(obj = stairs, option = "climb")) {
        on_obj_option(obj = stairs, option = "climb") {
            player.queue {
                when (options("Climb up the $name.", "Climb down the $name.")) {
                    1 -> player.moveTo(3104, 3161, player.tile.height + 1)
                    2 -> player.moveTo(3104, 3161, player.tile.height - 1)
                }
            }
        }
    }
    // Following 2 blocks handle ladders
    if (if_obj_has_option(obj = stairs, option = "climb-up")) {
        on_obj_option(obj = stairs, option = "climb-up") {
            player.moveTo(3104, 3161, player.tile.height + 1)
        }
    }
    if (if_obj_has_option(obj = stairs, option = "climb-down")) {
        on_obj_option(obj = stairs, option = "climb-down") {
            player.moveTo(3104, 3161, player.tile.height - 1)
        }
    }
}