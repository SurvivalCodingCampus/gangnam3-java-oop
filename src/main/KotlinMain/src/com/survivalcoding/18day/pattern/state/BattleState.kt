package com.survivalcoding.`18day`.pattern.state

import com.survivalcoding.`18day`.model.Pokemon

/**
 * [GoF State Pattern] 배틀 상태 인터페이스.
 *
 * 배틀의 각 단계(대기, 내 차례, 상대 차례, 배틀 종료)를 별도 클래스로 분리하여,
 * 배틀 로직에서 분기문을 제거하고 상태 객체에 위임합니다.
 *
 * OCP 준수: 새로운 배틀 상태 추가 시 기존 코드 수정 불필요.
 */
interface BattleState {

    /** 현재 상태의 이름을 반환합니다 (UI 표시용). */
    val stateName: String

    /** 상태 진입 시 호출됩니다. */
    fun onEnter(context: BattleContext)

    /** 상태 유지 중 호출됩니다. */
    fun onUpdate(context: BattleContext)

    /** 상태 종료 시 호출됩니다. */
    fun onExit(context: BattleContext)

    /**
     * 공격 명령을 처리합니다.
     * @return 다음 상태 (변화 없으면 null)
     */
    fun onAttack(context: BattleContext, attacker: Pokemon, defender: Pokemon): BattleState?

    /**
     * 배틀을 종료합니다.
     * @return 다음 상태 (변화 없으면 null)
     */
    fun onBattleEnded(context: BattleContext, isMyVictory: Boolean, winnerName: String): BattleState?

    /** 배틀이 활성 상태인지 여부 */
    fun isBattleActive(): Boolean = false

    /** 내 차례인지 여부 */
    fun isMyTurn(): Boolean = false
}

/**
 * 대기 상태: 포켓몬 선택 전, 배틀 준비 중.
 */
class WaitingState : BattleState {
    override val stateName: String = "대기 중"
    override fun onEnter(context: BattleContext) = Unit
    override fun onUpdate(context: BattleContext) = Unit
    override fun onExit(context: BattleContext) = Unit
    override fun onAttack(context: BattleContext, attacker: Pokemon, defender: Pokemon): BattleState? {
        return null // 대기 중에는 공격 불가
    }
    override fun onBattleEnded(context: BattleContext, isMyVictory: Boolean, winnerName: String): BattleState? {
        return null
    }
    override fun isBattleActive(): Boolean = false
    override fun isMyTurn(): Boolean = false
}

/**
 * 내 차례 상태: 사용자가 공격을 선택할 수 있음.
 */
class MyTurnState : BattleState {
    override val stateName: String = "내 차례"
    override fun onEnter(context: BattleContext) = Unit
    override fun onUpdate(context: BattleContext) = Unit
    override fun onExit(context: BattleContext) = Unit

    override fun onAttack(context: BattleContext, attacker: Pokemon, defender: Pokemon): BattleState? {
        return OpponentTurnState()
    }

    override fun onBattleEnded(context: BattleContext, isMyVictory: Boolean, winnerName: String): BattleState? {
        return BattleEndedState()
    }
    override fun isBattleActive(): Boolean = true
    override fun isMyTurn(): Boolean = true
}

/**
 * 상대 차례 상태: AI가 공격을 수행한 후 내 차례로 돌아옴.
 */
class OpponentTurnState : BattleState {
    override val stateName: String = "상대 차례"
    override fun onEnter(context: BattleContext) = Unit
    override fun onUpdate(context: BattleContext) = Unit
    override fun onExit(context: BattleContext) = Unit

    override fun onAttack(context: BattleContext, attacker: Pokemon, defender: Pokemon): BattleState? {
        return MyTurnState()
    }

    override fun onBattleEnded(context: BattleContext, isMyVictory: Boolean, winnerName: String): BattleState? {
        return BattleEndedState()
    }
    override fun isBattleActive(): Boolean = true
    override fun isMyTurn(): Boolean = false
}

/**
 * 배틀 종료 상태: 승리 또는 패배 판정 후.
 */
class BattleEndedState : BattleState {
    override val stateName: String = "배틀 종료"
    override fun onEnter(context: BattleContext) = Unit
    override fun onUpdate(context: BattleContext) = Unit
    override fun onExit(context: BattleContext) = Unit

    override fun onAttack(context: BattleContext, attacker: Pokemon, defender: Pokemon): BattleState? {
        return null // 배틀 종료 중에는 공격 불가
    }

    override fun onBattleEnded(context: BattleContext, isMyVictory: Boolean, winnerName: String): BattleState? {
        return null
    }
    override fun isBattleActive(): Boolean = false
    override fun isMyTurn(): Boolean = false
}

/**
 * [GoF State Pattern] 배틀 상태 컨텍스트.
 *
 * 현재 상태를 보유하고, 상태 전환을 관리합니다.
 * 상태 객체에 행동 대부분을 위임합니다.
 */
class BattleContext {

    var myPokemon: Pokemon? = null
    var opponentPokemon: Pokemon? = null

    private var currentState: BattleState = WaitingState()

    fun getState(): BattleState = currentState

    fun setState(newState: BattleState) {
        val oldState = currentState
        oldState.onExit(this)
        currentState = newState
        newState.onEnter(this)
    }

    fun isBattleActive(): Boolean = currentState.isBattleActive()
    fun isMyTurn(): Boolean = currentState.isMyTurn()
    fun stateName(): String = currentState.stateName

    fun onAttack(attacker: Pokemon, defender: Pokemon) {
        currentState.onAttack(this, attacker, defender)
    }

    fun onBattleEnded(isMyVictory: Boolean, winnerName: String) {
        currentState.onBattleEnded(this, isMyVictory, winnerName)
    }

    fun reset() {
        currentState.onExit(this)
        currentState = WaitingState()
        currentState.onEnter(this)
    }
}
