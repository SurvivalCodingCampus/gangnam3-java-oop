package com.survivalcoding.`18day`.game

import com.survivalcoding.`18day`.model.Pokemon

/**
 * [SRP] 게임 상태만 보유하는 모델 클래스.
 * UI 로직, 배틀 로직, 데이터 로드 로직을 포함하지 않음.
 */
class GameModel {
    var myPokemon: Pokemon? = null
    var opponentPokemon: Pokemon? = null
    var isMyTurn: Boolean = true
    var battleActive: Boolean = false

    fun reset() {
        myPokemon = null
        opponentPokemon = null
        isMyTurn = true
        battleActive = false
    }

    fun isBattleReady(): Boolean = myPokemon != null && opponentPokemon != null
}
