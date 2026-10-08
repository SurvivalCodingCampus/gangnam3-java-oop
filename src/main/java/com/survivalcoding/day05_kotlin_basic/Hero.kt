package com.survivalcoding.day05_kotlin_basic

open class Hero(
    protected val name: String,
    val nickname: String,
    hp: Int = MAX_HP,
) {
    var hp: Int = hp
        set(value) {
            require(value in 0..MAX_HP) {
                "HP는 0부터 $MAX_HP 사이여야 합니다: $value"
            }
            field = value
        }

    val isAlive: Boolean
        get() = hp > 0

    init {
        require(hp in 0..MAX_HP) {
            "HP는 0부터 $MAX_HP 사이여야 합니다: $hp"
        }
    }

    fun damage(amount: Int = 10) {
        if (amount <= 0) {
            return
        }

        hp = (hp - amount).coerceAtLeast(0)
        println("${name}의 HP: $hp")
    }

    fun sleep() {
        hp = MAX_HP
    }

    fun printInfo() {
        println("$name / $nickname / $hp")
    }

    open fun attack() {
        println("${name}의 공격")
    }

    open fun fly() {
        println("${name}의 비행")
    }

    companion object {
        const val MAX_HP = 100
    }
}