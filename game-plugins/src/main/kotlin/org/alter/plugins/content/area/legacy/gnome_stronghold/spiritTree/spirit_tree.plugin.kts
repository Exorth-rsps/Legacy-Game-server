package org.alter.plugins.content.objs.spirittree

val SPIRIT_TREES = arrayOf(Objs.SPIRIT_TREE_26260, Objs.SPIRIT_TREE_26261, Objs.SPIRIT_TREE_26263, Objs.SPIRIT_TREE_35950)
val TALKING_TREES = arrayOf(Objs.SPIRIT_TREE_26259, Objs.SPIRIT_TREE_26262, Objs.SPIRIT_TREE_35949)

on_login {
    //Unlock grand trees
    player.setVarp(111, 9)//Quest Tree Gnome Village
    player.setVarp(150, 160)//Quest The Grand Tree
    player.setVarbit(598, 2)
    player.playJingle(22)



}

TALKING_TREES.forEach { treeTalk ->
    on_obj_option(treeTalk, option = "talk-to") {
        if (treeTalk == 26259) {
            player.queue(TaskPriority.STRONG) {
                chatNpc("Need Npc Chat", npc = 4982)
            }
        } else {
            player.queue(TaskPriority.STRONG) {
                chatNpc("Need Npc Chat", npc = 4981)
            }
        }
    }
}

SPIRIT_TREES.forEach { tree ->
    on_obj_option(tree, "Travel") {
        TreeTele(player)
    }
    on_obj_option(tree, option = "talk-to") {
        if (tree == 26260 || tree == 26261) {
            player.queue(TaskPriority.STRONG) {
                chatNpc("Hello gnome friend. Where would you like to go?", npc = 4982)
                TreeTele(player)
            }
        } else {
            player.queue(TaskPriority.STRONG) {
                chatNpc("Hello gnome friend. Where would you like to go?", npc = 4981)
                TreeTele(player)
            }
        }
    }
}

fun spiritTreeTele(player: Player, endTile : Tile) {
    player.queue(TaskPriority.STRONG) {
        player.closeInterface(InterfaceDestination.MAIN_SCREEN)
        player.lock = LockState.DELAY_ACTIONS
        itemMessage(message = "You place your hands on the dry tough bark of the<br>spirit tree, and feel a surge of energy run through<br>your veins.", item = 6063, amountOrZoom = 400)
        player.animate(id = 828)
        wait(1)
        player.moveTo(endTile)
        wait(1)
        player.unlock()
        itemMessageBox(message = "You place your hands on the dry tough bark of the<br>spirit tree, and feel a surge of energy run through<br>your veins.", item = 6063, amountOrZoom = 400)
    }
}

fun TreeTele (player: Player) {
    player.queue(TaskPriority.STRONG) {

        when (
            interfaceOptions("Gnome Stronghold", "Draynor Rooftop", "<col=777777>Al Kharid Rooftop</col>", "<col=777777>Varrock Rooftop</col>", "<col=777777>Barbarian Outpost Course</col>", "<col=777777>Canfis Rooftop Course</col>", "Falador Rooftop Course", "<col=777777>Wilderness Course</col>", "Seers' Village Rooftop Course", "<col=777777>Pollnivneach Rooftop Course</col>", "<col=777777>Relekka Rooftop Course</col>", "<col=777777>Ardounge Rooftop Course</col>", "Cancel", title = "Spirit Tree Agility Locations")) {
            0 -> spiritTreeTele(player, Tile(2472, 3438, 0))//Gnome Stronghold
            1 -> spiritTreeTele(player, Tile(3105, 3278, 0))//Draynor Rooftop
            2 -> itemMessage(message = "This course unavailable.", item = 6063, amountOrZoom = 400)
            //2 -> spiritTreeTele(player, Tile(3273, 3198, 0))//Al kharid
            3 -> itemMessage(message = "This course unavailable.", item = 6063, amountOrZoom = 400)
            //3 -> spiritTreeTele(player, Tile(3223, 3416, 0))//Varrock
            4 -> itemMessage(message = "This course unavailable.", item = 6063, amountOrZoom = 400)
            //4 -> spiritTreeTele(player, Tile(2552, 3563, 0))//Barbarion
            5 -> itemMessage(message = "This course unavailable.", item = 6063, amountOrZoom = 400)
            //5 -> spiritTreeTele(player, Tile(3506, 3486, 0))//Canfis
            6 -> spiritTreeTele(player, Tile(3032, 3340, 0))//Falador
            7 -> itemMessage(message = "This course unavailable.", item = 6063, amountOrZoom = 400)
            //7 -> spiritTreeTele(player, Tile(2998, 3932, 0))//Wilderness
            8 -> spiritTreeTele(player, Tile(2731, 3484, 0))//Seers
            9 -> itemMessage(message = "This course unavailable.", item = 6063, amountOrZoom = 400)
            //9 -> spiritTreeTele(player, Tile(3356, 2965, 0))//Pollnivneach
            10 -> itemMessage(message = "This course unavailable.", item = 6063, amountOrZoom = 400)
            //10 -> spiritTreeTele(player, Tile(2631, 3678, 0))//Relekka
            11 -> itemMessage(message = "This course unavailable.", item = 6063, amountOrZoom = 400)
            //11 -> spiritTreeTele(player, Tile(2669, 3298, 0))//Ardounge
            12 -> player.closeInterface(InterfaceDestination.MAIN_SCREEN)
        }
    }
}