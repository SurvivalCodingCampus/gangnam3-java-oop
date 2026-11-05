package com.survivalcoding.day17_result.repository

import com.survivalcoding.day17_result.core.NetworkError
import com.survivalcoding.day17_result.core.Result
import com.survivalcoding.day17_result.model.Poke

interface PokeRepository {

    suspend fun getPoke(name: String): Result<Poke, NetworkError>
}