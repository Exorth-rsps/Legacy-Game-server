package org.alter.plugins.content.area.legacy.gnome_stronghold.objs

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */

on_obj_option(obj = Objs.STAIRCASE_16675, option = "climb-Up") {
    when(player.tile.z) {
        3414 -> { //bank
            player.queue {
                player.animate(828)
                player.lock()
                wait(2)
                player.moveTo(x = 2445, z = 3416, 1)
                player.unlock()
            }
        }
        3434 -> { //bank
            player.queue {
                player.animate(828)
                player.lock()
                wait(2)
                player.moveTo(x = 2445, z = 3433, 1)
                player.unlock()
            }
        }
        else ->  player.message("Nothing intresting happens...")
    }
}
on_obj_option(obj = Objs.STAIRCASE_16677, option = "climb-Down") {
    when(player.tile.z) {
        3416 -> { //bank
            player.queue {
                player.animate(828)
                player.lock()
                wait(2)
                player.moveTo(x = 2446, z = 3415, 0)
                player.unlock()
            }
        }
        3433 -> { //bank
            player.queue {
                player.animate(828)
                player.lock()
                wait(2)
                player.moveTo(x = 2444, z = 3434, 0)
                player.unlock()
            }
        }
        else ->  player.message("Nothing intresting happens...")
    }
}




