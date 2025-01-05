package org.alter.plugins.content.npcs.barbarians

import org.alter.plugins.content.combat.isBeingAttacked
import org.alter.plugins.content.drops.DropTableFactory

val barbarianIds = intArrayOf(
    Npcs.BARBARIAN,
    Npcs.BARBARIAN_3056,
    Npcs.BARBARIAN_3057,
    Npcs.BARBARIAN_3058,
    Npcs.BARBARIAN_3059,
    Npcs.BARBARIAN_3060,
    Npcs.BARBARIAN_3061,
    Npcs.BARBARIAN_3062,
)

val table = DropTableFactory
val barbarian =
    table.build {
        guaranteed {
            obj(Items.BONES)
            obj(Items.RAW_BEEF)
            obj(Items.COWHIDE)
        }
    }

table.register(barbarian, *barbarianIds)

on_npc_pre_death(*barbarianIds) {
    val p = npc.damageMap.getMostDamage()!! as Player
}

on_npc_death(*barbarianIds) {
    table.getDrop(world, npc.damageMap.getMostDamage()!! as Player, npc.id, npc.tile)
}

barbarianIds.forEach { barbarian ->
    on_npc_option(npc = barbarian, option = "talk-to") { player.queue { randomBarbarianDialogue() } }
}

barbarianIds.forEach {
    set_combat_def(it) {
        configs {
            attackSpeed = 4
            respawnDelay = 50
            poisonChance = 0.0
            venomChance = 0.0
        }
        stats {
            hitpoints = 24
            attack = 15
            strength = 14
            defence = 10
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 0
            defenceSlash = -3
            defenceCrush = -2
            defenceMagic = 0
            defenceRanged = 0
        }

        anims {
            attack = Animation.HUMAN_2H_STRAIGHT_SWORD_SLASH
            block = Animation.HUMAN_2H_STRAIGHT_SWORD_DEFEND
            death = Animation.HUMAN_DEATH
        }

        sound {
            attackSound = Sound.HUMAN_ATTACK
            blockSound = Sound.HUMAN_BLOCK_1
            deathSound = Sound.HUMAN_DEATH
        }
    }
}

suspend fun QueueTask.randomBarbarianDialogue() {
    val dialogues = listOf<suspend QueueTask.() -> Unit>(
        { dialogue1() },
        { dialogue2() },
        { dialogue3() },
        { dialogue4() },
        { dialogue5() }
    )
    dialogues.random().invoke(this)
}

suspend fun QueueTask.dialogue1() {
    chatPlayer("Hello.")
    chatNpc("What do you want, outsider?")
    chatPlayer("Just passing through. Nice village you have here.")
    chatNpc("Hah! Nice? This place is for warriors, not tourists.")
}

suspend fun QueueTask.dialogue2() {
    chatNpc("Are you strong enough to wield a blade?")
    chatPlayer("I think so. Why?")
    chatNpc("Because weaklings don't last long here. Show some strength or leave.")
    chatPlayer("I'll keep that in mind.")
}

suspend fun QueueTask.dialogue3() {
    chatPlayer("What do you barbarians do for fun?")
    chatNpc("Fun? We drink, fight, and train. That's all the fun we need!")
    chatPlayer("Sounds... intense.")
    chatNpc("Hah! It's the only way to live.")
}

suspend fun QueueTask.dialogue4() {
    chatNpc("Our ancestors watch us. Every battle, every step.")
    chatPlayer("That sounds inspiring.")
    chatNpc("Inspiring? Its a reminder not to fail. Failure brings shame to the clan.")
    chatPlayer("I see. I'll remember that.")
}

suspend fun QueueTask.dialogue5() {
    chatPlayer("Do you train every day?")
    chatNpc("Every hour we're awake. Strength doesn't come from sitting around.")
    chatPlayer("That explains why you're all so strong.")
    chatNpc("And why you'd better not make us angry.")
}

