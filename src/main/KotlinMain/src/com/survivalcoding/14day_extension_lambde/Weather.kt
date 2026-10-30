package com.survivalcoding.`14day_extension_lambde`

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

// 1. [메인 프로그램]: 마법사들의 모임 시작!
suspend fun main() {
    // Weathers라는 날씨 요정에게 내일 날씨를 물어봅니다.
    val data = Weathers.tomorrow()
    println("내일 날씨는: ${data}")
}

// 2. [날씨 서비스 객체 (object)]:
// 세상에 딱 하나만 존재하며, 날씨와 관련된 기능들을 모아두는 '날씨 창고'입니다.
object Weathers {
    // 비어 있는 것처럼 보이지만, 이 안에 날씨와 관련된 함수(기능)들을 담아두고 꺼내 쓸 수 있어요!
}

// 3. [확장 함수]: Weathers 창고에 '내일 날씨를 알려주는 능력(tomorrow)'을 살짝 얹어줍니다.
// suspend 키워드가 붙어 있어서, 날씨를 알아내는 동안(2초) 컴퓨터가 멈추지 않고 다른 일을 할 수 있어요!
suspend fun Weathers.tomorrow(): String {
    // 2초(2000밀리초) 동안 하늘을 보며 생각에 잠깁니다 (스레드를 차단하지 않고 양보하는 delay 사용)
    delay(2000L.milliseconds)

    // 2초가 지나면 "맑음"이라는 날씨 결과를 척척 알려줍니다!
    return "맑음"
}