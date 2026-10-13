package com.survivalcoding.day12kotlin

sealed interface BattleEvent {
    data class RoundStarted(val round: Int) : BattleEvent

    data class AttackPerformed(val result: AttackResult) : BattleEvent

    data class OpponentDefeated(
        val name: String,
        val rewardExp: Int,
    ) : BattleEvent

    data class PartyVictory(val totalExp: Int) : BattleEvent

    data object PartyDefeat : BattleEvent
}

object BattleLog {
    fun format(event: BattleEvent): String =
        when (event) {
            is BattleEvent.RoundStarted -> "=== ${event.round} 라운드 ==="
            is BattleEvent.AttackPerformed -> with(event.result) {
                "$attackerName: $attackName -> ${targetName}에게 ${damage} 피해 (HP $remainingHp)"
            }
            is BattleEvent.OpponentDefeated ->
                "${event.name}을(를) 처치했습니다. 경험치 ${event.rewardExp} 획득!"
            is BattleEvent.PartyVictory -> "파티 승리! 총 경험치: ${event.totalExp}"
            BattleEvent.PartyDefeat -> "파티가 패배했습니다."
        }
}
