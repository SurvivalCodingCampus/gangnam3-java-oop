package com.survivalcoding.day18_http_exam.repository

import com.survivalcoding.day18_http_exam.data_source.PokemonDataSource
import com.survivalcoding.day18_http_exam.dto.PokemonDto
import com.survivalcoding.day18_http_exam.mapper.toModel
import com.survivalcoding.day18_http_exam.model.Pokemon

// 먼저 짠다 (비즈니스 핵심)
// 로직만 볼거야. 기술 관심 없어(역직렬화? 관심 없어)
// 걸러내기 (filter)
class PokemonRepositoryImpl(
    private val dataSource: PokemonDataSource
) : PokemonRepository {

    override suspend fun getPokemon(name: String): Pokemon? {
        try {
            val response = dataSource.getPokemon(name)

            val dto: PokemonDto = response.body ?: return null

            return dto.toModel()

        } catch (ex: Exception) {
            return null
        }
    }
}