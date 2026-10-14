package com.survivalcoding.day12_class_generic

// 읽기와 쓰기를 모두 제공하므로 out/in 없이 일반 <E> 사용
class Pocket<E> {
    private var _data: E? = null

    fun put(data: E) {
        _data = data
    }

    fun get(): E? = _data
}

fun main() {
    val pocket = Pocket<Int>()
    val storedNumber = 1_111
    pocket.put(storedNumber)

    // Int로 정한 Pocket에 String을 넣으면 컴파일 오류 (타입 안전성)
    // 꺼낼 때 별도 캐스팅 없이 Int?로 받을 수 있음
    val data: Int? = pocket.get()
    println(data)
}
