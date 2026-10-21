package com.survivalcoding.day12kotlin

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class YukymController {

    // [버그 수정 1]: 소문자 mm은 '분(minute)'을 뜻하므로, '월(month)'을 뜻하는 대문자 MM으로 수정했습니다! (yyyy-MM-dd)
    val nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    lateinit var nowTime: String

    // 현재 월에 따라 국(경오국)을 판별하는 함수
    fun getTyA(): String {
        val timeDataOne = _getTimeDataOne(nowDate)

        if (timeDataOne.isNotEmpty()) {
            nowTime = timeDataOne.first().ty1

            // 월 정보를 앞에서 5~7번째 글자로 잘라내어 가져옴
            val month = nowDate.substring(5, 7)

            // when을 사용하여 월별 국 지정
            return when (month) {
                "01", "02" -> "경오1국"
                "03", "04" -> "경오2국"
                "05", "06" -> "경오3국"
                "07", "08" -> "경오4국"
                "09", "10" -> "경오5국"
                "11", "12" -> "경오6국"
                else -> nowTime
            }
        } else {
            return "경오7국"
        }
    }

    // 현재 시간에 따라 해당하는 시간대별 타이틀을 리턴하는 함수
    fun getTyB(): String {
        val timeDataOne = _getTimeDataOne(nowDate)
        val result = timeDataOne.first().ty12

        val nowTime = LocalDateTime.now()
        val currentHour = nowTime.hour // 현재 시각 (0~23시)

        // [버그 수정 2 & 3]:
        // 1. OR(||) 연산자를 쓰면 항상 참이 되므로, '이상 이고 미만'을 뜻하는 AND(&&) 연산자로 모두 교체했습니다.
        // 2. 2시~4시 구간(`2 이상 이고 4 미만`)이 빠져있던 누락 구간을 채워 넣었습니다.
        when {
            currentHour in 0 until 2  -> return timeDataOne.first().ty1   // 0시 ~ 1시 59분
            currentHour in 2 until 4  -> return timeDataOne.first().ty12  // 2시 ~ 3시 59분 (누락 구간 복구)
            currentHour in 4 until 6  -> return timeDataOne.first().ty2   // 4시 ~ 5시 59분
            currentHour in 6 until 8  -> return timeDataOne.first().ty3   // 6시 ~ 7시 59분
            currentHour in 8 until 10 -> return timeDataOne.first().ty4   // 8시 ~ 9시 59분
            currentHour in 10 until 12 -> return timeDataOne.first().ty5  // 10시 ~ 11시 59분
            currentHour in 12 until 14 -> return timeDataOne.first().ty6  // 12시 ~ 13시 59분
            currentHour in 14 until 16 -> return timeDataOne.first().ty11 // (예시 구간)
            currentHour in 16 until 18 -> return timeDataOne.first().ty7  // 16시 ~ 17시 59분
            currentHour in 18 until 20 -> return timeDataOne.first().ty8  // 18시 ~ 19시 59분
            currentHour in 20 until 22 -> return timeDataOne.first().ty9  // 20시 ~ 21시 59분
            currentHour in 22 until 24 -> return timeDataOne.first().ty10 // 22시 ~ 23시 59분
        }

        return result
    }

    // [개선 포인트]: 매번 불필요하게 25개의 객체를 만들던 반복문을, 필요한 단 하나의 모델만 생성하도록 가볍고 단순하게 다듬었습니다.
    private fun _getTimeDataOne(nowDate: String): List<YukymTimeModel> {
        return listOf(YukymTimeModel())
    }
}

// 데이터를 담는 기본 모델 클래스
data class YukymTimeModel(
    val ty1: String = "갑자1국",
    val ty2: String = "갑자2국",
    val ty3: String = "갑자3국",
    val ty4: String = "갑자4국",
    val ty5: String = "갑자5국",
    val ty6: String = "갑자6국",
    val ty7: String = "갑자7국",
    val ty8: String = "갑자8국",
    val ty9: String = "갑자9국",
    val ty10: String = "갑자10국",
    val ty11: String = "갑자11국",
    val ty12: String = "갑자12국"
)