package com.survivalcoding.generic_enum_string

import com.survivalcoding.day12kotlin.AdvancedMonster
import com.survivalcoding.day12kotlin.Attackable
import com.survivalcoding.day12kotlin.BattleGame
import com.survivalcoding.day12kotlin.BattleLog
import com.survivalcoding.day12kotlin.Flyableimport com.survivalcoding.day12kotlin.Hero
import com.survivalcoding.day12kotlin.MagicCaster
import com.survivalcoding.day12kotlin.Monster
import com.survivalcoding.day12kotlin.SpiderMan
import com.survivalcoding.day12kotlin.Wizard
import com.survivalcoding.day12kotlin.Zombie

fun createDemoOutput(): List<String> {
    val hero = Hero("용사", hp = 80)
    val wizard = Wizard("마법사", mp = 30, hp = 60)
    val monster = Monster("고블린", hp = 30, attackDamage = 7)
    val advancedMonster = AdvancedMonster(
        "고대 골렘",
        hp = 100,
        attackDamage = 15,
        rewardExp = 300,
    )

    val events = BattleGame().play(
        party = listOf(hero, wizard),
        opponents = listOf(advancedMonster, monster),
    )

    val practiceTarget = Monster("훈련용 허수아비", hp = 100, attackDamage = 0)
    val spiderMan = SpiderMan("스파이더맨")
    val attackables: List<Attackable> = listOf(
        Hero("연습 용사", hp = 20),
        Zombie("연습 좀비", damage = 2),
        spiderMan,
    )
    val polymorphicResults = attackables.map { it.attack(practiceTarget) }

    val flyer: Flyable = spiderMan
    val takeOff = flyer.fly()
    val landing = spiderMan.land()

    return buildList {
        add("[결정적 전투 데모]")
        addAll(events.map(BattleLog::format))
        add("최종 상태: 용사 HP ${hero.hp}, 마법사 HP ${wizard.hp}, MP ${wizard.mp}")
        add("[Attackable 다형성]")
        addAll(polymorphicResults.map { "${it.attackerName}: ${it.attackName}" })
        add("[인터페이스 상속] $takeOff $landing")
        add("상수: 최대 공격 ${Attackable.MAX_ATTACK_PER_TURN}회, 기본 MP ${MagicCaster.BASE_MP}")
    }
}

fun main() {
    createDemoOutput().forEach(::println)
}
