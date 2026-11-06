package com.survivalcoding.`18day`.repostory

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.Data_source.PokemonDataSource
import com.survivalcoding.`18day`.mapper.toModel
import com.survivalcoding.`18day`.model.Pokemon

// 포켓몬 레포지토리 구현체: 데이터 소스를 주입받아 DTO를 도메인 모델로 변환하여 반환
class PokemonRepositoryImpl(
    private val dataSource: PokemonDataSource
) : PokemonRepository {

    override suspend fun getPokemon(name: String): Result<Pokemon, NetworkError> {
        return when (val result = dataSource.getPokemon(name)) {
            is Result.Success -> Result.Success(result.value.toModel())
            is Result.Error -> Result.Error(result.error)
            is Result.Loading -> Result.Loading(null)
            is Result.Id -> Result.Id(result.value.toModel())
            is Result.Failure -> Result.Failure(result.error)
        }
    }
}
