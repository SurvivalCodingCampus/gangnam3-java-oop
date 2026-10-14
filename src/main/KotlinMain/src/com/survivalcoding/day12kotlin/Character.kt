package com.survivalcoding.day12kotlin

abstract class Character(
    val name: String,
    hp: Int,
) {
    var hp: Int = hp
        private set

    val isAlive: Boolean
        get() = hp > 0

    init {
        require(name.isNotBlank()) { "이름은 비어 있을 수 없습니다." }
        require(hp >= 0) { "HP는 0 이상이어야 합니다." }
    }

    abstract fun attack(target: Character): AttackResult

    fun takeDamage(damage: Int): Int {
        require(damage >= 0) { "데미지는 0 이상이어야 합니다." }
        val previousHp = hp
        hp = (hp - damage).coerceAtLeast(0)
        return previousHp - hp
    }
}
