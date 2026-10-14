package com.survivalcoding.day12_class_generic

import java.math.BigInteger

class StrongBox<T>(private val keyType: KeyType) {

    // null 췤
    private var storedThing: T? = null

    // 안정성 보완: BigInteger는 고정된 숫자 범위가 없어 Int/Long처럼 음수로 넘치지 않음
    private var attemptCount: BigInteger = BigInteger.ZERO

    // 한번 해제된 잠금은 유지, put()으로 내용물을 바꿔도 다시 잠그지 않음
    private var isUnlocked: Boolean = false

    // 열쇠별 횟수는 KeyType에서 관리하고 비교할 숫자는 생성할 때 한 번 변환
    private val requiredAttempts: BigInteger =
        BigInteger.valueOf(keyType.requiredAttempts.toLong())

    // put(): 같은 타입의 인스턴스 1개를 보관
    fun put(thing: T) {
        // 마지막에 넣은 1개만 보관, 시도 횟수와 잠금 상태는 유지
        storedThing = thing
    }

    // get(): T?에 잠금 확인을 추가
    fun get(): T? {
        // 내용물을 반환하기 전에 현재 호출도 시도 횟수에 포함
        attemptCount = attemptCount.add(BigInteger.ONE)

        // 강의의 '횟수에 도달하면 열린다'를 적용: N번째 호출부터 잠금 해제
        if (!isUnlocked && attemptCount >= requiredAttempts) {
            isUnlocked = true
        }

        // 잠겨 있거나 내용물이 없으면 null, 열렸으면 저장한 타입 그대로 반환 null 췤
        // get()은 내용물을 확인하는 동작이므로 저장한 인스턴스를 삭제하지 않음
        return if (isUnlocked) storedThing else null
    }
}

enum class KeyType(val requiredAttempts: Int) {
    padlock(1_024),
    button(10_000),
    dial(30_000),
    finger(1_000_000)
}
