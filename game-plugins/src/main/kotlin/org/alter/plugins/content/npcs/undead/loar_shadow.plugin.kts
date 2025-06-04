package org.alter.plugins.content.npcs.undead

// When a Loar Shadow is attacked it turns into a Loar Shade

on_npc_combat(Npcs.LOAR_SHADOW) {
    if (npc.getTransmogId() != Npcs.LOAR_SHADE) {
        npc.setTransmogId(Npcs.LOAR_SHADE)
    }
}

set_combat_def(Npcs.LOAR_SHADOW) {
    configs {
        attackSpeed = 4
        respawnDelay = 35
        poisonChance = 0.0
        venomChance = 0.0
    }
    stats {
        hitpoints = 38
        attack = 45
        strength = 30
        defence = 26
        magic = 1
        ranged = 1
    }
    anims {
        attack = Animation.SHADE_ATTACK
        block = Animation.SHADE_HIT
        death = Animation.SHADE_DEATH
    }
    sound {
        attackSound = Sound.SHADE_ATTACK
        blockSound = Sound.SHADE_HIT
        deathSound = Sound.SHADE_DEATH
    }
}

