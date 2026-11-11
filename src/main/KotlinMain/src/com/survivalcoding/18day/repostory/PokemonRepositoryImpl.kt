package com.survivalcoding.`18day`.repostory

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.Data_source.PokemonDataSource
<<<<<<< HEAD
import com.survivalcoding.`18day`.model.Pokemon

/**
 * [SRP/ISP] 모든 분리된 인터페이스를 구현하는 저장소 구현체.
 *
 * 원격 데이터 소스 전용 기능(배치 조회, 캐시, 종료)은 현재
 * RemotePokemonDataSource가 Ktor 의존성으로 컴파일 제외된 상태이므로,
 * 가짜 데이터 소스에 맞춰 단순화된 구현을 제공합니다.
 */
class PokemonRepositoryImpl(
    private val dataSource: PokemonDataSource
) : IPokemonRepository,
    IPokemonBatchRepository,
    IPokemonCacheManager,
    ICloseableResource {

    override fun getPokemon(name: String): Result<Pokemon, NetworkError> {
        return when (val result = dataSource.getPokemon(name)) {
            is Result.Success -> Result.Success(result.value)
            is Result.Error -> Result.Error(result.error)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    override fun getPokemonBatch(names: List<String>): Map<String, Result<Pokemon, NetworkError>> {
        // 원격 데이터 소스 전용 기능은 현재 컴파일 제외 상태이므로,
        // 가짜 데이터 소스 기반으로 개별 조회를 배치처럼 반환합니다.
        return names.associate { name -> name to getPokemon(name) }
    }

    override fun getCachedPokemonNames(): List<String> {
        return emptyList()
    }

    override fun clearCache() {
        // 가짜 데이터 소스는 캐시를 가지지 않음
    }

    override fun cacheSize(): Int {
        return 0
    }

    override fun close() {
        // 가짜 데이터 소스는 정리할 리소스가 없음
    }
=======
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
>>>>>>> e52e4e0b7368ddf43cae584263b85b4acee4797b
}
