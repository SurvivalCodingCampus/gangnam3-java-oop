package com.survivalcoding.`18day`.repostory

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.model.Pokemon

/**
 * [SRP] 포켓몬 단건 조회 전용 인터페이스
 */
interface IPokemonRepository {
    fun getPokemon(name: String): Result<Pokemon, NetworkError>
}

/**
 * [SRP] 배치 조회 전용 인터페이스
 */
interface IPokemonBatchRepository {
    fun getPokemonBatch(names: List<String>): Map<String, Result<Pokemon, NetworkError>>
}

/**
 * [SRP] 캐시 관리 전용 인터페이스
 */
interface IPokemonCacheManager {
    fun getCachedPokemonNames(): List<String>
    fun clearCache()
    fun cacheSize(): Int
}

/**
 * [SRP] 리소스 정리 전용 인터페이스
 */
interface ICloseableResource {
    fun close()
}
