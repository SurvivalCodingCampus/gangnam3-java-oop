package com.survivalcoding.kotlin_basic


fun main() {
    println("Hello World")

    val name: String? = "null"
    println(name?.length)

    val i : Int = 10
    val d : Double = i.toDouble()

    // 타입 추론
    val hero = KotlinHero("name", hp = 10)
    println(hero.name)

    val cleric = KotlinCleric(name = "성직자")

    val message: String = if (hero.hp > 0) {
        "아직 싸울 수 있습니다"
    } else {
        "전투 불능입니다"
    }

    var name1: String = "이름1"
    var name2: String? = "이름2"

    // String = String? (X)
    name1 = name2 ?: "이름 없음" // (O)

    if (name2 != null) {
        name1 = name2 // smart cast
    } else {
        name1 = "이름 없음"
    }

    println(name1)

    var name3: String? = null

//    name3 = "홍길동"

    // 버그 가능성 생성
//    var name4: String = name3!! // 얜 이제 널이 아님을 내(인간)가 보증한다

    var name4: String = name3 ?: "null 임"

    // List<Integer> nums = new ArrayList<Int>();

    // 바보 취급
//    val nums2: List<Int> = ArrayList<Int>()

    val nums = mutableListOf(1, 2, 3)
    nums.add(1)
    nums.add(2)
    println(nums)
}

open class KotlinHero(
    var name: String,
    hp: Int = 100,
) {
    var hp: Int = hp    // this.hp = hp
        set(value) {
//            if (value < 0) {
//                throw IllegalAccessException("hp 가 음수면 안 되면")
//            }
            require(value in 0..MAX_HP) {
                "HP는 0부터 $MAX_HP 사이여야 합니다: $value"
            }
            field = value
        }

    // public void
    open fun attack() {
        println("$name : $hp")
    }

    // public static final
    companion object {
        const val MAX_HP = 100
    }
}

class KotlinCleric(
    val name: String,
    var hp: Int = MAX_HP,
    var mp: Int = MAX_MP,
) {
    // 생성자 후에 호출
    init {
        println("호출!!!!!!!!!")
    }

    companion object {
        const val MAX_HP = 50
        const val MAX_MP = 10
        const val MP_COST = 5
    }

    fun selfAid() {
        if (mp < MP_COST) {
            return
        }

        mp -= MP_COST
        hp = MAX_HP
    }

    fun pray(seconds: Int): Int {
        val bonus = (0..2).random()     // 0 ~ 2
//        val bonum = Random().nextInt(3)   // 0 ~ 2
        val recovery = seconds + bonus
        val maxRecovery = MAX_MP - mp
//        val actureRecovery = Math.min(recovery, maxRecovery)
        val actualRecovery = minOf(recovery, maxRecovery)

        mp += actualRecovery
        return actualRecovery
    }
}

interface Moveable {
    fun move()
}

class SuperHero(name: String) : KotlinHero(name), Moveable {
    override fun attack() {
        super.attack()
    }

    override fun move() {
        println("이동했어요")
    }
}

// 함수
fun add(a: Int, b: Int): Int {
    return a + b
}