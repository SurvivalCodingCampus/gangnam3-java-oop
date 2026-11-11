package com.survivalcoding.`18day`.pattern.proxy

import com.survivalcoding.`18day`.Data_source.PokemonDataSource
import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.model.Pokemon

/**
 * [GoF Proxy Pattern] 캐싱 프록시 데이터 소스.
 *
 * 실제 PokemonDataSource(원격 API 등)를 감싸고, 조회 결과를 캐시에 저장하여
 * 중복 API 호출을 줄입니다. 클라이언트는 실제 데이터 소스와 프록시를
 * 동일한 인터페이스(PokemonDataSource)를 통해 사용하므로 투명하게 교체 가능합니다.
 *
 * SRP 준수: 캐싱 로직만 담당, 실제 데이터 조회는 위임 대상에 위임.
 * OCP 준수: 새로운 데이터 소스가 추가되어도 프록시 코드 수정 불필요.
 */
class CachingDataSourceProxy(
    private val target: PokemonDataSource,
    private val maxCacheSize: Int = 100
) : PokemonDataSource {

    private val cache = java.util.concurrent.ConcurrentHashMap<String, Pokemon>()

    override fun getPokemon(name: String): Result<Pokemon, NetworkError> {
        val key = name.trim().lowercase()

        // 1. 캐시 히트
        cache.get(key)?.let {
            return Result.Success(it)
        }

        // 2. 캐시 미스 → 대상 데이터 소스에서 조회
        val result = target.getPokemon(key)

        // 3. 성공 시 캐시에 저장 (최대 크기 초과 시 가장 오래된 항목 제거 - 단순 LRU 근사)
        if (result is Result.Success) {
            if (cache.size >= maxCacheSize) {
                val firstKey = cache.keys.firstOrNull()
                if (firstKey != null) {
                    cache.remove(firstKey)
                }
            }
            cache[key] = result.value
        }

        return result
    }

    fun getCachedKeys(): List<String> = cache.keys.toList()

    fun clearCache() {
        cache.clear()
    }

    fun cacheSize(): Int = cache.size

    fun evict(name: String) {
        cache.remove(name.trim().lowercase())
    }

    companion object {
        @JvmStatic
        fun create(target: PokemonDataSource): CachingDataSourceProxy =
            CachingDataSourceProxy(target)

        @JvmStatic
        fun create(target: PokemonDataSource, maxCacheSize: Int): CachingDataSourceProxy =
            CachingDataSourceProxy(target, maxCacheSize)
    }
}
