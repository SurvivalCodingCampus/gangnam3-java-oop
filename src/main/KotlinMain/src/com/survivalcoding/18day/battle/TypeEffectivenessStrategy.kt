package com.survivalcoding.`18day`.battle

/**
 * 타입 상성 전략 인터페이스.
 * 새로운 타입 추가 시 기존 코드 수정 없이 이 인터페이스를 구현한 클래스를 추가하기만 하면 됩니다 (OCP).
 */
interface TypeEffectivenessStrategy {
    /**
     * 공격 타입과 방어 타입에 따른 상성 배율을 반환합니다.
     * @return 2.0 = 효과 탁월, 0.5 = 효과 별로, 1.0 = 보통
     */
    fun getEffectiveness(attackType: String, defendType: String): Double
}

/**
 * 기본 타입 상성 전략 구현체.
 * 불/물/풀/전기/노말 타입의 상성 테이블을 정의합니다.
 */
class DefaultTypeEffectivenessStrategy : TypeEffectivenessStrategy {

    override fun getEffectiveness(attackType: String, defendType: String): Double {
        return when {
            // 불타입 상성
            attackType == "fire" && defendType == "grass" -> 2.0
            attackType == "fire" && defendType == "fire" -> 0.5
            attackType == "fire" && defendType == "water" -> 0.5
            // 물타입 상성
            attackType == "water" && defendType == "fire" -> 2.0
            attackType == "water" && defendType == "water" -> 0.5
            attackType == "water" && defendType == "grass" -> 0.5
            // 풀타입 상성
            attackType == "grass" && defendType == "water" -> 2.0
            attackType == "grass" && defendType == "grass" -> 0.5
            attackType == "grass" && defendType == "fire" -> 0.5
            // 전기타입 상성
            attackType == "electric" && defendType == "water" -> 2.0
            attackType == "electric" && defendType == "electric" -> 0.5
            attackType == "electric" && defendType == "grass" -> 0.5
            // 노말타입 상성
            attackType == "normal" -> 1.0
            else -> 1.0
        }
    }
}
