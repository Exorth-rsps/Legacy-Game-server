package org.alter.plugins.content.area.taverley.dungeon

/**
 * @author Eikenb00m <https://github.com/eikenb00m>
 */


on_obj_option(obj = Objs.PORTCULLIS_21772, option = "Exit") {
    if (player.getSkills().getCurrentLevel(Skills.SLAYER) > 89) {
        player.queue {
            player.moveTo(tile = Tile(x = 2873, z = 9847, height = 0))
        }
    } else {
        player.message("How you get here? You need a slayer of 90 to get here!")
    }
}
on_obj_option(obj = Objs.CAVE_26567, option = "Crawl") {
    if (player.getSkills().getCurrentLevel(Skills.SLAYER) > 89) {
        player.queue {
            player.moveTo(tile = Tile(x = 1304, z = 1290, height = 0))
            player.message("You enterd Cerberus' Lair.")
        }
    } else {
        player.message("You need a Slayer of 90 to crawl trough!")
    }
}
on_obj_option(obj = Objs.CAVE_26568, option = "Crawl") {
    if (player.getSkills().getCurrentLevel(Skills.SLAYER) > 89) {
        player.queue {
            player.moveTo(tile = Tile(x = 1304, z = 1290, height = 0))
            player.message("You enterd Cerberus' Lair.")
        }
    } else {
        player.message("You need a Slayer of 90 to crawl trough!")
    }
}
on_obj_option(obj = Objs.CAVE_26569, option = "Crawl") {
    if (player.getSkills().getCurrentLevel(Skills.SLAYER) > 89) {
        player.queue {
            player.moveTo(tile = Tile(x = 1304, z = 1290, height = 0))
            player.message("You enterd Cerberus' Lair.")
        }
    } else {
        player.message("You need a Slayer of 90 to crawl trough!")
    }
}