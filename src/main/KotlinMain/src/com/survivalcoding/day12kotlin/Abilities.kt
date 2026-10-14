package com.survivalcoding.day12kotlin

interface Attackable {
    fun attack(target: Character): AttackResult

    companion object {
        const val MAX_ATTACK_PER_TURN: Int = 3
    }
}

interface Flyable {
    fun fly(): String
}

interface SuperFlyable : Flyable {
    fun land(): String
}

interface MagicCaster {
    fun castMagic(target: Character): AttackResult

    companion object {
        const val BASE_MP: Int = 50
    }
}
