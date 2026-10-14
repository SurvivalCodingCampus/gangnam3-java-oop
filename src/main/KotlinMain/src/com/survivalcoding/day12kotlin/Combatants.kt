package com.survivalcoding.day12kotlin

class Hero(
    name: String,
    hp: Int,
    private val attackDamage: Int = 7,
) : Character(name, hp), Attackable {
    init {
        require(attackDamage >= 0) { "공격력은 0 이상이어야 합니다." }
    }

    override fun attack(target: Character): AttackResult =
        performAttack(target, attackDamage, "검 공격")
}

open class Monster(
    name: String,
    hp: Int,
    val attackDamage: Int,
) : Character(name, hp), Attackable {
    init {
        require(attackDamage >= 0) { "공격력은 0 이상이어야 합니다." }
    }

    override fun attack(target: Character): AttackResult =
        performAttack(target, attackDamage, "할퀴기")
}

class AdvancedMonster(
    name: String,
    hp: Int,
    attackDamage: Int,
    val rewardExp: Int,
) : Monster(name, hp, attackDamage) {
    init {
        require(rewardExp >= 0) { "보상 경험치는 0 이상이어야 합니다." }
    }

    override fun attack(target: Character): AttackResult =
        performAttack(target, attackDamage, "강화 공격")

    fun die(): String {
        takeDamage(hp)
        return "${name}을(를) 처치했습니다. 경험치 $rewardExp 획득!"
    }
}

class Wizard(
    name: String,
    mp: Int = MagicCaster.BASE_MP,
    hp: Int,
    private val staffDamage: Int = 1,
    private val magicDamage: Int = 27,
) : Character(name, hp), Attackable, MagicCaster {
    var mp: Int = mp
        private set

    init {
        require(mp >= 0) { "MP는 0 이상이어야 합니다." }
        require(staffDamage >= 0) { "지팡이 공격력은 0 이상이어야 합니다." }
        require(magicDamage >= 0) { "마법 공격력은 0 이상이어야 합니다." }
    }

    override fun attack(target: Character): AttackResult =
        performAttack(target, staffDamage, "지팡이 공격")

    override fun castMagic(target: Character): AttackResult {
        check(mp >= MAGIC_COST) {
            "${name}의 마력이 부족합니다. 필요: $MAGIC_COST, 보유: $mp"
        }
        mp -= MAGIC_COST
        return performAttack(target, magicDamage, "화염구")
    }

    companion object {
        const val MAGIC_COST: Int = 10
    }
}

private fun Character.performAttack(
    target: Character,
    damage: Int,
    attackName: String,
): AttackResult {
    val inflictedDamage = target.takeDamage(damage)
    return AttackResult(name, target.name, inflictedDamage, target.hp, attackName)
}
