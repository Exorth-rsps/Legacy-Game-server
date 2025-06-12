package org.alter.plugins.content.npcs.barrows

import org.alter.api.cfg.Animation
import org.alter.api.cfg.Npcs
import org.alter.api.cfg.Sound

set_combat_def(Npcs.DHAROK_THE_WRETCHED) {
    configs {
        attackSpeed = 4
        respawnDelay = 0
        poisonChance = 0.0
        venomChance = 0.0
    }
    stats {
        hitpoints = 100
        attack = 100
        strength = 100
        defence = 100
        magic = 1
        ranged = 1
    }
    bonuses {
        defenceStab = 50
        defenceSlash = 50
        defenceCrush = 50
        defenceMagic = 40
        defenceRanged = 40
    }
    anims {
        attack = Animation.HUMAN_DHAROKS_GREATAXE_SWING
        block = Animation.HUMAN_DEFEND
        death = Animation.HUMAN_DEATH
    }
    sound {
        attackSound = Sound.DHAROK_ATTACK
        blockSound = Sound.HUMAN_BLOCK_1
        deathSound = Sound.HUMAN_DEATH
    }
}

set_combat_def(Npcs.VERAC_THE_DEFILED) {
    configs {
        attackSpeed = 4
        respawnDelay = 0
        poisonChance = 0.0
        venomChance = 0.0
    }
    stats {
        hitpoints = 100
        attack = 100
        strength = 100
        defence = 100
        magic = 1
        ranged = 1
    }
    bonuses {
        defenceStab = 50
        defenceSlash = 50
        defenceCrush = 50
        defenceMagic = 40
        defenceRanged = 40
    }
    anims {
        attack = Animation.HUMAN_VERACS_FLAIL_ATTACK
        block = Animation.HUMAN_VERACS_FLAIL_DEFEND
        death = Animation.HUMAN_DEATH
    }
    sound {
        attackSound = Sound.VERAC_ATTACK
        blockSound = Sound.HUMAN_BLOCK_1
        deathSound = Sound.HUMAN_DEATH
    }
}

set_combat_def(Npcs.AHRIM_THE_BLIGHTED) {
    configs {
        attackSpeed = 4
        respawnDelay = 0
        poisonChance = 0.0
        venomChance = 0.0
    }
    stats {
        hitpoints = 100
        attack = 1
        strength = 1
        defence = 100
        magic = 100
        ranged = 1
    }
    bonuses {
        defenceStab = 40
        defenceSlash = 40
        defenceCrush = 40
        defenceMagic = 50
        defenceRanged = 40
    }
    anims {
        attack = Animation.HUMAN_AHRIMS_STAFF_ATTACK
        block = Animation.HUMAN_AHRIMS_STAFF_DEFEND
        death = Animation.HUMAN_DEATH
    }
    sound {
        attackSound = Sound.AHRIM_ATTACK
        blockSound = Sound.HUMAN_BLOCK_1
        deathSound = Sound.HUMAN_DEATH
    }
}

set_combat_def(Npcs.GUTHAN_THE_INFESTED) {
    configs {
        attackSpeed = 4
        respawnDelay = 0
        poisonChance = 0.0
        venomChance = 0.0
    }
    stats {
        hitpoints = 100
        attack = 100
        strength = 100
        defence = 100
        magic = 1
        ranged = 1
    }
    bonuses {
        defenceStab = 50
        defenceSlash = 50
        defenceCrush = 50
        defenceMagic = 40
        defenceRanged = 40
    }
    anims {
        attack = Animation.HUMAN_GUTHANS_WARSPEAR_STAB
        block = Animation.HUMAN_DEFEND
        death = Animation.HUMAN_DEATH
    }
    sound {
        attackSound = Sound.GUTHAN_ATTACK
        blockSound = Sound.HUMAN_BLOCK_1
        deathSound = Sound.HUMAN_DEATH
    }
}

set_combat_def(Npcs.KARIL_THE_TAINTED) {
    configs {
        attackSpeed = 4
        respawnDelay = 0
        poisonChance = 0.0
        venomChance = 0.0
    }
    stats {
        hitpoints = 100
        attack = 1
        strength = 1
        defence = 100
        magic = 1
        ranged = 100
    }
    bonuses {
        defenceStab = 40
        defenceSlash = 40
        defenceCrush = 40
        defenceMagic = 40
        defenceRanged = 50
    }
    anims {
        attack = Animation.HUMAN_KARILS_CROSSBOW_ATTACK
        block = Animation.HUMAN_DEFEND
        death = Animation.HUMAN_DEATH
    }
    sound {
        attackSound = Sound.KARIL_ATTACK
        blockSound = Sound.HUMAN_BLOCK_1
        deathSound = Sound.HUMAN_DEATH
    }
}

set_combat_def(Npcs.TORAG_THE_CORRUPTED) {
    configs {
        attackSpeed = 4
        respawnDelay = 0
        poisonChance = 0.0
        venomChance = 0.0
    }
    stats {
        hitpoints = 100
        attack = 100
        strength = 100
        defence = 100
        magic = 1
        ranged = 1
    }
    bonuses {
        defenceStab = 50
        defenceSlash = 50
        defenceCrush = 50
        defenceMagic = 40
        defenceRanged = 40
    }
    anims {
        attack = Animation.HUMAN_TORAGS_HAMMERS_SWING
        block = Animation.HUMAN_DEFEND
        death = Animation.HUMAN_DEATH
    }
    sound {
        attackSound = Sound.TORAG_ATTACK
        blockSound = Sound.HUMAN_BLOCK_1
        deathSound = Sound.HUMAN_DEATH
    }
}
