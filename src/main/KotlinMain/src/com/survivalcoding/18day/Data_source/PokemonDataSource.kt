package com.survivalcoding.`18day`.Data_source

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
<<<<<<< HEAD
import com.survivalcoding.`18day`.model.Pokemon

interface PokemonDataSource {
    fun getPokemon(name: String): Result<Pokemon, NetworkError>
=======
import com.survivalcoding.`18day`.dto.PokemonDto

// 포켓몬 데이터 소스 인터페이스: 원격 또는 가짜 데이터 소스를 추상화
interface PokemonDataSource {
    suspend fun getPokemon(name: String): Result<PokemonDto, NetworkError>
>>>>>>> e52e4e0b7368ddf43cae584263b85b4acee4797b
}
