package com.survivalcoding.`18day`.pattern.observer

import com.survivalcoding.`18day`.model.Pokemon

/**
 * [GoF Observer Pattern] 배틀 이벤트.
 *
 * 배틀 중에 발생하는 모든 이벤트를 캡슐화한 클래스입니다.
 * 옵저버는 이 이벤트를 수신하여 UI 업데이트, 사운드 재생, 로그 기록 등을 수행합니다.
 */
sealed class BattleEvent {

    /** 포켓몬 로드 완료 */
    data class PokemonLoaded(val isMy: Boolean, val pokemon: Pokemon) : BattleEvent()

    /** 포켓몬 로드 실패 */
    data class LoadError(val message: String) : BattleEvent()

    /** 배틀 시작 준비 완료 */
    class BattleReady : BattleEvent()

    /** 차례 변경 */
    data class TurnChanged(val isMyTurn: Boolean) : BattleEvent()

    /** 공격 실행됨 */
    data class AttackExecuted(
        val message: String,
        val isMyTurn: Boolean,
        val attacker: Pokemon,
        val defender: Pokemon,
        val damage: Int
    ) : BattleEvent()

    /** 포켓몬 상태 업데이트 (HP 감소 등) */
    data class PokemonUpdated(val isMy: Boolean, val pokemon: Pokemon) : BattleEvent()

    /** 배틀 종료 */
    data class BattleEnded(val isMyVictory: Boolean, val winnerName: String) : BattleEvent()

    /** 상태 메시지 변경 */
    data class StatusChanged(val message: String) : BattleEvent()

    /** 배틀 초기화 */
    class BattleReset : BattleEvent()
}
