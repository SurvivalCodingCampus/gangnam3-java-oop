package com.survivalcoding.`18day`.test.pattern

import com.survivalcoding.`18day`.model.Pokemon
import com.survivalcoding.`18day`.pattern.factory.PokemonRepositoryFactory
import com.survivalcoding.`18day`.pattern.factory.PokemonRepositoryFactory.DataSourceKind

/**
 * GoF Factory Pattern 전용 테스트
 */
class FactoryPatternTest {

    var passCount = 0
    var failCount = 0
    private val results = mutableListOf<Pair<String, Boolean>>()

    fun runAll() {
        println("========== [Factory Pattern Test 시작] ==========")
        println()

        testFakeRepositoryCreation()
        testRemoteRepositoryCreation()
        testDataSourceInjection()

        println()
        println("========== [테스트 결과 요약] ==========")
        for ((name, passed) in results) {
            val status = if (passed) "✅ 통과" else "❌ 실패"
            println("  $status | $name")
            if (passed) passCount++ else failCount++
        }
        println()
        println("총 테스트: ${results.size}, 통과: $passCount, 실패: $failCount")
        println("========== [Factory Pattern Test 종료] ==========")
    }

    private fun record(name: String, passed: Boolean, detail: String = "") {
        results += name to passed
        println("[테스트] $name: ${if (passed) "통과" else "실패"} $detail")
    }

    private fun testFakeRepositoryCreation() {
        val repo = PokemonRepositoryFactory.createRepository(DataSourceKind.FAKE)
        val result = repo.getPokemon("pikachu")
        record("Factory - 가짜 저장소에서 pikachu 조회",
            result is com.survivalcoding.`18day`.core.Result.Success &&
                (result as com.survivalcoding.`18day`.core.Result.Success<Pokemon>).value.name == "pikachu")
    }

    private fun testRemoteRepositoryCreation() {
        val repo = PokemonRepositoryFactory.createRepository(DataSourceKind.REMOTE)
        record("Factory - 원격 저장소 인스턴스 생성",
            repo ::class.java.simpleName == "PokemonRepositoryImpl")
    }

    private fun testDataSourceInjection() {
        val repo = PokemonRepositoryFactory.createRepository(
            com.survivalcoding.`18day`.Data_source.FakePokemonDataSource()
        )
        val result = repo.getPokemon("charizard")
        record("Factory - DataSource 직접 주입",
            result is com.survivalcoding.`18day`.core.Result.Success &&
                (result as com.survivalcoding.`18day`.core.Result.Success<Pokemon>).value.name == "charizard")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            FactoryPatternTest().runAll()
        }
    }
}
