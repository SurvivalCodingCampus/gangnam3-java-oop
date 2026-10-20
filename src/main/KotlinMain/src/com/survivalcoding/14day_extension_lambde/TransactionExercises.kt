package com.survivalcoding.extension

data class Trader(val name: String, val city: String)

data class Transaction(val trader: Trader, val year: Int, val value: Int)

fun main() {
    val transactions = listOf(
        Transaction(Trader("Brian", "Cambridge"), 2011, 300),
        Transaction(Trader("Raoul", "Cambridge"), 2012, 1000),
        Transaction(Trader("Raoul", "Cambridge"), 2011, 400),
        Transaction(Trader("Mario", "Milan"), 2012, 710),
        Transaction(Trader("Mario", "Milan"), 2012, 700),
        Transaction(Trader("Alan", "Cambridge"), 2012, 950)
    )

    // 1. 2011년에 일어난 모든 트랜잭션을 찾아 가격 기준 오름차순으로 정리하여 이름 나열
    val q1 = transactions
        .filter { it.year == 2011 }
        .sortedBy { it.value }
        .map { it.trader.name }
    println("1. $q1")

    // 2. 거래자가 근무하는 모든 도시를 중복 없이 나열
    val q2 = transactions
        .map { it.trader.city }
        .distinct()
    println("2. $q2")

    // 3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하여 나열
    val q3 = transactions
        .map { it.trader }
        .filter { it.city == "Cambridge" }
        .distinct()
        .sortedBy { it.name }
    println("3. $q3")

    // 4. 모든 거래자의 이름을 알파벳순으로 정렬하여 나열
    val q4 = transactions
        .map { it.trader.name }
        .distinct()
        .sorted()
    println("4. $q4")

    // 5. 밀라노에 거래자가 있는가?
    val q5 = transactions
        .any { it.trader.city == "Milan" }
    println("5. $q5")

    // 6. 케임브리지에 거주하는 거래자의 모든 트랜잭션값 출력
    val q6 = transactions
        .filter { it.trader.city == "Cambridge" }
        .map { it.value }
    println("6. $q6")

    // 7. 전체 트랜잭션 중 최댓값 (maxOfOrNull 사용)
    val q7 = transactions.maxOfOrNull { it.value } ?: 0
    println("7. $q7")

    // 8. 전체 트랜잭션 중 최솟값 (minOfOrNull 사용)
    val q8 = transactions.minOfOrNull { it.value } ?: 0
    println("8. $q8")
}