package com.survivalcoding.`18day`.pattern.observer

import com.survivalcoding.`18day`.model.Pokemon

/**
 * [GoF Observer Pattern] 배틀 이벤트 발행자 (Subject).
 *
 * 배틀 로직에서 발생하는 이벤트를 등록된 모든 옵저버(UI, 로그, 사운드 등)에
 * 알립니다. 주체와 옵저버가 느슨하게 결합되어 있어,
 * 옵저버를 추가/제거해도 주체 코드를 수정할 필요가 없습니다 (OCP, DIP).
 */
interface BattleObservable {

    fun registerObserver(observer: BattleObserver)

    fun unregisterObserver(observer: BattleObserver)

    fun notifyObservers(event: BattleEvent)
}

/**
 * [GoF Observer Pattern] 배틀 이벤트 수신자 (Observer).
 *
 * UI, 로그, 사운드 매니저 등이 이 인터페이스를 구현하여
 * 배틀 이벤트에 반응합니다.
 */
interface BattleObserver {
    fun onBattleEvent(event: BattleEvent)
}

/**
 * [GoF Observer Pattern] 기본 BattleObservable 구현체.
 *
 * 옵저버 목록을 관리하고 이벤트를 브로드캐스트합니다.
 */
abstract class AbstractBattleObservable : BattleObservable {

    private val observers = mutableListOf<BattleObserver>()

    override fun registerObserver(observer: BattleObserver) {
        if (observer !in observers) {
            observers.add(observer)
        }
    }

    override fun unregisterObserver(observer: BattleObserver) {
        observers.remove(observer)
    }

    override fun notifyObservers(event: BattleEvent) {
        // 복사본을 순회하여 알림 중 옵저버 해제 문제 방지
        observers.toList().forEach { it.onBattleEvent(event) }
    }

    protected fun pokemonLoaded(isMy: Boolean, pokemon: Pokemon) {
        notifyObservers(BattleEvent.PokemonLoaded(isMy, pokemon))
    }

    protected fun loadError(message: String) {
        notifyObservers(BattleEvent.LoadError(message))
    }

    protected fun battleReady() {
        notifyObservers(BattleEvent.BattleReady())
    }

    protected fun turnChanged(isMyTurn: Boolean) {
        notifyObservers(BattleEvent.TurnChanged(isMyTurn))
    }

    protected fun attackExecuted(
        message: String,
        isMyTurn: Boolean,
        attacker: Pokemon,
        defender: Pokemon,
        damage: Int
    ) {
        notifyObservers(
            BattleEvent.AttackExecuted(message, isMyTurn, attacker, defender, damage)
        )
    }

    protected fun pokemonUpdated(isMy: Boolean, pokemon: Pokemon) {
        notifyObservers(BattleEvent.PokemonUpdated(isMy, pokemon))
    }

    protected fun battleEnded(isMyVictory: Boolean, winnerName: String) {
        notifyObservers(BattleEvent.BattleEnded(isMyVictory, winnerName))
    }

    protected fun statusChanged(message: String) {
        notifyObservers(BattleEvent.StatusChanged(message))
    }

    protected fun battleReset() {
        notifyObservers(BattleEvent.BattleReset())
    }
}
