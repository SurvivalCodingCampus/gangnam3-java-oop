package com.survivalcoding.`18day`.test.pattern

import com.survivalcoding.`18day`.Data_source.FakePokemonDataSource
import com.survivalcoding.`18day`.pattern.proxy.CachingDataSourceProxy

/**
 * GoF Proxy Pattern 테스트
 *
 * CachingDataSourceProxy의 캐싱 동작을 검증합니다.
 */
class ProxyPatternTest {

    var passCount = 0
    var failCount = 0
    private val results = mutableListOf<Pair<String, Boolean>>()

    fun runAll() {
        println("========== [Proxy Pattern Test 시작] ==========")
        println()

        testCacheHit()
        testCacheMiss()
        testCacheEviction()
        testCacheClear()
        testMaxCacheSize()

        println()
        println("========== [테스트 결과 요약] ==========")
        for ((name, passed) in results) {
            val status = if (passed) "✅ 통과" else "❌ 실패"
            println("  $status | $name")
            if (passed) passCount++ else failCount++
        }
        println()
        println("총 테스트: ${results.size}, 통과: $passCount, 실패: $failCount")
        println("========== [Proxy Pattern Test 종료] ==========")
    }

    private fun record(name: String, passed: Boolean, detail: String = "") {
        results += name to passed
        println("[테스트] $name: ${if (passed) "통과" else "실패"} $detail")
    }

    private fun testCacheHit() {
        val dataSource = FakePokemonDataSource()
        val proxy = CachingDataSourceProxy.create(dataSource)

        // 첫 번째 조회: 캐시 미스 → 데이터 소스에서 조회
        val result1 = proxy.getPokemon("pikachu")
        val hit1 = result1 is com.survivalcoding.`18day`.core.Result.Success<*>

        // 두 번째 조회: 캐시 히트
        val result2 = proxy.getPokemon("pikachu")
        val hit2 = result2 is com.survivalcoding.`18day`.core.Result.Success<*>

        record("Proxy - 캐시 히트/미스",
            hit1 && hit2 && proxy.cacheSize() == 1)
    }

    private fun testCacheMiss() {
        val dataSource = FakePokemonDataSource()
        val proxy = CachingDataSourceProxy.create(dataSource)

        val result = proxy.getPokemon("nonexistent-pokemon")
        val isError = result is com.survivalcoding.`18day`.core.Result.Error<*>

        // 오류 결과는 캐시에 저장되지 않아야 함
        record("Proxy - 오류 결과 미캐싱",
            isError && proxy.cacheSize() == 0)
    }

    private fun testCacheEviction() {
        val dataSource = FakePokemonDataSource()
        val proxy = CachingDataSourceProxy.create(dataSource, maxCacheSize = 2)

        proxy.getPokemon("pikachu")
        proxy.getPokemon("charizard")
        // cache size = 2, max = 2
        record("Proxy - 캐시 최대치 도달",
            proxy.cacheSize() == 2)

        proxy.getPokemon("bulbasaur") // 가장 오래된 항목(pikachu) 퇴출 예상
        record("Proxy - 캐시 퇴출 (최대 크기 초과)",
            proxy.cacheSize() <= 2)
    }

    private fun testCacheClear() {
        val dataSource = FakePokemonDataSource()
        val proxy = CachingDataSourceProxy.create(dataSource)

        proxy.getPokemon("pikachu")
        proxy.getPokemon("charizard")
        record("Proxy - 캐시 항목 2개", proxy.cacheSize() == 2)

        proxy.clearCache()
        record("Proxy - 캐시 클리어", proxy.cacheSize() == 0)
    }

    private fun testMaxCacheSize() {
        val dataSource = FakePokemonDataSource()
        val proxy = CachingDataSourceProxy.create(dataSource, maxCacheSize = 10)

        for (name in listOf("pikachu", "charizard", "bulbasaur", "squirtle", "jigglypuff")) {
            proxy.getPokemon(name)
        }
        record("Proxy - maxCacheSize=10, 5개 조회",
            proxy.cacheSize() == 5)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ProxyPatternTest().runAll()
        }
    }
}
