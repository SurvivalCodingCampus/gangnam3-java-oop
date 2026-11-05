package com.survivalcoding.`18day`.Data_source

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.dto.PokemonDto

// 포켓몬 데이터 소스 인터페이스: 원격 또는 가짜 데이터 소스를 추상화
interface PokemonDataSource {
    suspend fun getPokemon(name: String): Result<PokemonDto, NetworkError>
}
