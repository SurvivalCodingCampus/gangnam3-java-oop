package com.survivalcoding.day16_dto_mapper.repository

import com.survivalcoding.day16_dto_mapper.data_source.PokeDataSource
import com.survivalcoding.day16_dto_mapper.mapper.toModel
import com.survivalcoding.day16_dto_mapper.model.Poke
import kotlin.coroutines.cancellation.CancellationException

class PokeRepositoryImpl(
    private val dataSource: PokeDataSource
) : PokeRepository {

    override suspend fun getPoke(name: String): Poke? {
        return try {
            val poke = dataSource.getPoke(name)
            poke?.toModel()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }
}