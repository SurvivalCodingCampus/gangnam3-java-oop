package com.survivalcoding.day12kotlin

class Zombie(
    val name: String,
    private val damage: Int,
) : Attackable {
    init {
        require(name.isNotBlank()) { "이름은 비어 있을 수 없습니다." }
        require(damage >= 0) { "공격력은 0 이상이어야 합니다." }
    }

    override fun attack(target: Character): AttackResult {
        val inflictedDamage = target.takeDamage(damage)
        return AttackResult(name, target.name, inflictedDamage, target.hp, "물어뜯기")
    }
}

class SpiderMan(
    val name: String,
    private val damage: Int = 5,
) : SuperFlyable, Attackable {
    var isFlying: Boolean = false
        private set

    init {
        require(name.isNotBlank()) { "이름은 비어 있을 수 없습니다." }
        require(damage >= 0) { "공격력은 0 이상이어야 합니다." }
    }

    override fun fly(): String {
        isFlying = true
        return "$name 이(가) 웹을 쏘아 날아올랐습니다."
    }

    override fun land(): String {
        isFlying = false
        return "$name 이(가) 착륙했습니다."
    }

    override fun attack(target: Character): AttackResult {
        val inflictedDamage = target.takeDamage(damage)
        return AttackResult(name, target.name, inflictedDamage, target.hp, "거미줄 공격")
    }
}
