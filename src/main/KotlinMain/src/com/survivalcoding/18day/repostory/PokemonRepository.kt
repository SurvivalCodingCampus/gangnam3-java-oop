package com.survivalcoding.`18day`.repostory

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.model.Pokemon

// 포켓몬 레포지토리 인터페이스: 도메인 계층에서 정의하는 포켓몬 데이터 접근 계약
interface PokemonRepository {
    suspend fun getPokemon(name: String): Result<Pokemon, NetworkError>
}
