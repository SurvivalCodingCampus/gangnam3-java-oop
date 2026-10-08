package com.survivalcoding.day05_kotlin_basic

fun main() {
    val hero = Hero(name = "홍길동", nickname = "Mr.Hong", hp = 100)
    // printlnln(hero.name)

    hero.hp = 50
    println("${hero.nickname}의 HP: ${hero.hp}")

    hero.run {
        damage()
        damage(amount = 30)
        sleep()
    }

    println(hero.hp)

    // hero = Hero("홍길동", "Mr.Hong", 100)

    val member = PartyMember(hp = 100)
    member.hp = 50
    println(member.hp)

    // member = PartyMember(30)

    val cleric1 = Cleric(name = "세실")
    val cleric2 = Cleric(name = "로사", hp = 30)
    val cleric3 = Cleric(name = "세실", hp = 40, mp = 5)

    val critical = true
    val damage = if (critical) 20 else 10
    println(damage)

    val message = if (hero.hp > 0) {
        "아직 싸울 수 있습니다"
    } else {
        "전투 불능입니다"
    }
    println(message)

    val name = "홍길동"
    var nickname: String? = null

    // val length = nickname.length
    val length: Int? = nickname?.length
    println(length)

    val displayName = nickname ?: "이름 없음"
    println(displayName)

    // val length1 = nickname!!.length
    // println(length1)

    // if (hero is SuperHero) {
    //     hero.fly()
    // }

    val party = mutableListOf<Hero>()
    party.add(Hero("홍길동", "Mr.홍"))
    party.add(SuperHero("이순신", "충무공", 100))

    for (hero in party) {
        hero.attack()
        if (hero is SuperHero) {
            hero.fly()
        }
    }

    val party1 = listOf(
        Hero("홍길동", "Mr.홍"),
        SuperHero("이순신", "충무공", 100)
    )
    repeat(party1.size) { index ->
        party1[index].attack()
    }

    val names = party.map { hero -> hero.nickname }
    println(names)
}
