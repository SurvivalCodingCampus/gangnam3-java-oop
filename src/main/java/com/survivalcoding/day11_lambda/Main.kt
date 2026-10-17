package com.survivalcoding.day11_lambda

import kotlin.math.max
import kotlin.math.min

fun main() {
    // 함수를 값으로 전달하는 예
    val list = listOf(1, 2, 3)
    list.forEach(::printElement)

    // 메서드와 함수의 차이
    list.forEach({ e -> println(e) })
    list.forEach { e -> println(e) }

    // 람다식
    val loudify = { msg: String -> "!!! ${msg.uppercase()} !!!" }
    assert(loudify("hello") == "!!! HELLO !!!")

    // 함수형 인터페이스 또는 Single Abstract Method (SAM) 인터페이스
    list.sortedWith(object : Comparator<Int> {
        override fun compare(o1: Int, o2: Int): Int {
            return o1 - o2
        }
    })

    list.sortedWith { o1, o2 -> o1 - o2 } // 함수가 하나만 있는 인터페이스는 람다로 변경 가능

    val timer = Timer()

    timer.start(object : OnCompleteCallback {
        override fun onComplete() {
            println("땡")
        }
    })

    timer.start {
        println("땡")
    }

    timer.start(::onComplete)

    val downloader = Downloader()

    downloader.download { progress ->
        println("다운로드 진행률: $progress%")
    }

    val items = listOf(1, 2, 3, 4, 5)

    for (item in items) {
        if (item % 2 == 0) {
            println(item)
        }
    }

    items.filter { it % 2 == 0 }
        .map { "숫자 $it" }
        .forEach(::println)

    val itemz = listOf(1, 2, 2, 3, 3, 4, 5)
    val temp = mutableSetOf<Int>()

    for (item in itemz) {
        if (item % 2 == 0) {
            temp.add(item)
        }
    }

    val result = temp.toList()

    println(result)

    itemz.filter { it % 2 == 0 }
        .toSet()
        .toList()

    itemz.filter { it % 2 == 0 }
        .distinct()

    var isResult = false

    for (item in itemz) {
        if (item % 2 == 0) {
            isResult = true
            break
        }
    }

    println(isResult)

    println(itemz.any { it % 2 == 0 })

    var maxResult = items[0]

    for (item in items) {
        maxResult = max(maxResult, item)
    }

    println(maxResult)

    println(items.reduce { acc, i -> max(acc, i) })
    println(items.reduce(::max))

    // 안티 패턴 - forEach 에서 중단 시도
    list.forEach { item ->
        if (true) {
            return@forEach
        }
        // 처리
    }

    // 올바른 접근 방법 = 전통적인 for 문 사용
    for (item in list) {
        if (true) break
        // 처리
    }

    // 안티 패턴 - map 에서 중단 시도 및 예외
    list.map { item ->
        if (true) {
            return@map null
        }
        // 처리
    }

    // list.map { item ->
    //     if (true) {
    //         throw Exception("중단")
    //     }
    //     // 처리
    // }

    // 연습문제 - 쿼리 (Query)
    val transactions = listOf(
        Transaction(Trader("Brian", "Cambridge"), 2011, 300),
        Transaction(Trader("Raoul", "Cambridge"), 2012, 1000),
        Transaction(Trader("Raoul", "Cambridge"), 2011, 400),
        Transaction(Trader("Mario", "Milan"), 2012, 710),
        Transaction(Trader("Mario", "Milan"), 2012, 700),
        Transaction(Trader("Alan", "Cambridge"), 2012, 950),
    )

    // 2011년에 일어난 모든 트랜잭션을 찾아 가격 기준 오름차순으로 정리하여 이름을 나열한다.
    transactions.filter { it.year == 2011 }
        .sortedBy { it.value }
        .forEach { println(it.trader.name) }

    // 거래자가 근무하는 모든 도시를 중복 없이 나열한다.
    transactions.map { it.trader.city }
        .distinct()
        .forEach(::println)

    // 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하여 나열한다.
    transactions.filter { it.trader.city == "Cambridge" }
        .map { it.trader }
        .distinct()
        .sortedBy { it.name }
        .forEach(::println)

    // 모든 거래자의 이름을 알파벳순으로 정렬하여 나열
    transactions.map { it.trader.name }
        .distinct()
        .sortedBy { it }
        .forEach(::println)

    // 밀라노에 거래자가 있는가?
    println(transactions.any { it.trader.city == "Milan" })

    // 케임브리지에 거주하는 거래자의 모든 트랙잭션 값을 출력
    transactions.filter { it.trader.city == "Cambridge" }
        .map { it.value }
        .forEach(::println)

    // 전체 트랜잭션 중 (가격) 최대값은 얼마인가?
    val maxTransactions = transactions.map { it.value }
        .reduce(::max)

    println(maxTransactions)

    // 전체 트랜잭션 중 (가격) 최소값은 얼마인가?
    val minTransactions = transactions.map { it.value }
        .reduce(::min)

    println(minTransactions)
}

fun onComplete() {
    println("땡")
}

fun printElement(element: Int) {
    println(element)
}