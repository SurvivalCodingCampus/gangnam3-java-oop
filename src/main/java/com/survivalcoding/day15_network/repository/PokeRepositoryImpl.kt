package com.survivalcoding.day15_network.repository

import com.survivalcoding.day15_network.datasource.PokeDataSource
import com.survivalcoding.day15_network.model.Pokemon
import kotlin.coroutines.cancellation.CancellationException

class PokeRepositoryImpl(
    private val dataSource: PokeDataSource
) : PokeRepository {

    override suspend fun getPokemon(name: String): Pokemon? {
        return try {
            val response = dataSource.getPokemon(name)
            if (response.statusCode in 200..299) response.body else null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}