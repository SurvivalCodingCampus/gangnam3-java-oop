package com.survivalcoding.`15day_dataSource`

import com.survivalcoding.day15_network.model.Pokemon
import com.survivalcoding.day15_network.model.Response

// 테스트에서 상태 코드, 데이터, 예외를 정해 Repository가 받을 응답을 만든다
class FakePokeDataSource : PokeDataSource {

    var statusCode: Int = 200
    var pokemons: List<Pokemon> = emptyList()
    var exception: Exception? = null

    private val isSuccess: Boolean get() = statusCode in 200..299

    override suspend fun getPokemon(name: String): Response<Pokemon?> {
        exception?.let { throw it }
        return Response(statusCode = statusCode, body = if (isSuccess) pokemons.find { it.name == name } else null)
    }
}