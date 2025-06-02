package org.alter.plugins.content.mechanics.doors

import org.alter.game.model.World
import org.alter.game.model.collision.ObjectType
import org.alter.game.model.entity.GameObject
import org.alter.game.model.entity.Player
import org.alter.game.model.attr.AttributeKey
import kotlin.runCatching

val STICK_STATE = AttributeKey<DoorStickState>()

val CHANGES_BEFORE_STICK_TAG = "opens_before_stick"
val RESET_STICK_DELAY_TAG   = "reset_stuck_doors_delay"

/** Safely get a property or return a default als hij niet ingesteld is */
inline fun <T> safeProperty(key: String, default: T): T =
    runCatching { getProperty<T>(key)!! }.getOrDefault(default)

val changesBeforeStick: Int
    get() = safeProperty(CHANGES_BEFORE_STICK_TAG, 5)

val resetStickDelay: Int
    get() = safeProperty(RESET_STICK_DELAY_TAG, 100)

load_service(DoorService())

on_world_init {
    val service = world.getService(DoorService::class.java)!!

    // ─────── Enkelvoudige deuren ───────
    service.doors.forEach { door ->

        // CLOSE (van opened → closed → auto open)
        on_obj_option(obj = door.opened, option = "close") {
            val p = player
            val w = world

            w.queue {
                // 1) verwijder open deur
                val oldObj = p.getInteractingGameObj()
                w.remove(oldObj)

                // 2) spawn closed deur
                val closedObj = w.closeDoor(
                    oldObj,
                    closed = door.closed,
                    invertTransform = oldObj.type == ObjectType.DIAGONAL_WALL.value
                )
                copy_stick_vars(oldObj, closedObj)
                add_stick_var(w, closedObj)

                p.playSound(Sound.CLOSE_DOOR_SFX)
                p.message("You close the door.")

                // 3) wacht tot automatische heropening
                wait(resetStickDelay)

                // 4) verwijder closed deur
                w.remove(closedObj)

                // 5) respawn open deur
                val reopened = w.openDoor(
                    closedObj,
                    opened = door.opened,
                    invertTransform = closedObj.type == ObjectType.DIAGONAL_WALL.value
                )
                copy_stick_vars(closedObj, reopened)
                p.playSound(Sound.OPEN_DOOR_SFX)
            }
        }

        // OPEN (van closed → open → auto close)
        on_obj_option(obj = door.closed, option = "open") {
            val p = player
            val w = world

            w.queue {
                // 1) verwijder gesloten deur
                val oldObj = p.getInteractingGameObj()
                w.remove(oldObj)

                // 2) spawn open deur
                val openedObj = w.openDoor(
                    oldObj,
                    opened = door.opened,
                    invertTransform = oldObj.type == ObjectType.DIAGONAL_WALL.value
                )
                copy_stick_vars(oldObj, openedObj)
                add_stick_var(w, openedObj)

                p.playSound(Sound.OPEN_DOOR_SFX)
                p.message("You open the door.")

                // 3) wacht tot automatische hersluiting
                wait(resetStickDelay)

                // 4) verwijder open deur
                w.remove(openedObj)

                // 5) respawn closed deur
                val reclosed = w.closeDoor(
                    openedObj,
                    closed = door.closed,
                    invertTransform = openedObj.type == ObjectType.DIAGONAL_WALL.value
                )
                copy_stick_vars(openedObj, reclosed)
                p.playSound(Sound.CLOSE_DOOR_SFX)
            }
        }
    }

    // ─────── Dubbele deuren (ongewijzigd) ───────
    service.doubleDoors.forEach { doors ->
        on_obj_option(obj = doors.closed.left, option = "open") {
            handle_double_doors(player, player.getInteractingGameObj(), doors, open = true)
        }
        on_obj_option(obj = doors.closed.right, option = "open") {
            handle_double_doors(player, player.getInteractingGameObj(), doors, open = true)
        }
        on_obj_option(obj = doors.opened.left, option = "close") {
            handle_double_doors(player, player.getInteractingGameObj(), doors, open = false)
        }
        on_obj_option(obj = doors.opened.right, option = "close") {
            handle_double_doors(player, player.getInteractingGameObj(), doors, open = false)
        }
    }
}

fun handle_double_doors(p: Player, obj: GameObject, doors: DoubleDoorSet, open: Boolean) {
    val left = obj.id == doors.opened.left || obj.id == doors.closed.left
    val right = obj.id == doors.opened.right || obj.id == doors.closed.right
    check(left || right)

    val otherDoorId = if (open) {
        if (left) doors.closed.right else doors.closed.left
    } else {
        if (left) doors.opened.right else doors.opened.left
    }
    val otherDoor = get_neighbour_door(world, obj, otherDoorId) ?: return

    if (!open && (is_stuck(world, obj) || is_stuck(world, otherDoor))) {
        p.message("The door seems to be stuck.")
        p.playSound(Sound.STUCK_DOOR_SFX)
        return
    }

    if (open) {
        val door1 = world.openDoor(obj, opened = if (left) doors.opened.left else doors.opened.right, invertRot = left)
        val door2 = world.openDoor(otherDoor, opened = if (left) doors.opened.right else doors.opened.left, invertRot = right)
        copy_stick_vars(obj, door1); add_stick_var(world, door1)
        copy_stick_vars(obj, door2); add_stick_var(world, door2)
        p.playSound(Sound.OPEN_DOOR_SFX)
    } else {
        val door1 = world.closeDoor(obj, closed = if (left) doors.closed.left else doors.closed.right, invertRot = left,  invertTransform = left)
        val door2 = world.closeDoor(otherDoor, closed = if (left) doors.closed.right else doors.closed.left, invertRot = right, invertTransform = right)
        copy_stick_vars(obj, door1); add_stick_var(world, door1)
        copy_stick_vars(obj, door2); add_stick_var(world, door2)
        p.playSound(Sound.CLOSE_DOOR_SFX)
    }
}

fun get_neighbour_door(world: World, obj: GameObject, otherDoor: Int): GameObject? {
    val tile = obj.tile
    for (x in -1..1) for (z in -1..1) {
        if (x == 0 && z == 0) continue
        val transform = tile.transform(x, z)
        val tileObj = world.getObject(transform, type = obj.type)
        if (tileObj?.id == otherDoor) return tileObj
    }
    return null
}

fun copy_stick_vars(from: GameObject, to: GameObject) {
    if (from.attr.has(STICK_STATE)) {
        to.attr[STICK_STATE] = from.attr[STICK_STATE]!!
    }
}

fun add_stick_var(world: World, obj: GameObject) {
    var current = get_stick_changes(obj)
    if (obj.attr.has(STICK_STATE) &&
        kotlin.math.abs(world.currentCycle - obj.attr[STICK_STATE]!!.lastChangeCycle) >= resetStickDelay
    ) current = 0

    obj.attr[STICK_STATE] = DoorStickState(current + 1, world.currentCycle)
}

fun get_stick_changes(obj: GameObject): Int =
    obj.attr[STICK_STATE]?.changeCount ?: 0

fun is_stuck(world: World, obj: GameObject): Boolean {
    val stuck = get_stick_changes(obj) >= changesBeforeStick
    if (stuck &&
        kotlin.math.abs(world.currentCycle - obj.attr[STICK_STATE]!!.lastChangeCycle) >= resetStickDelay
    ) {
        obj.attr.remove(STICK_STATE)
        return false
    }
    return stuck
}
