package com.survivalcoding.`18day`.repostory

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.model.Pokemon

interface PokemonRepository {
    fun getPokemon(name: String): Result<Pokemon, NetworkError>

    /**
     * 여러 포켓몬을 배치로 조회합니다.
     */
    fun getPokemonBatch(names: List<String>): Map<String, Result<Pokemon, NetworkError>>

    /**
     * 캐시에 저장된 포켓몬 이름 목록을 반환합니다.
     */
    fun getCachedPokemonNames(): List<String>

    /**
     * 캐시를 모두 비웁니다.
     */
    fun clearCache()

    /**
     * 캐시 항목 수를 반환합니다.
     */
    fun cacheSize(): Int

    /**
     * 리소스(HTTP 클라이언트 등)를 정리합니다.
     */
    fun close()
}
