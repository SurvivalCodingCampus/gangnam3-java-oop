package com.survivalcoding.day18_http_exam

import com.survivalcoding.day18_http_exam.core.NetworkError
import com.survivalcoding.day18_http_exam.core.Result
import com.survivalcoding.day18_http_exam.data_source.PokemonDataSource
import com.survivalcoding.day18_http_exam.data_source.RemotePokemonDataSource
import com.survivalcoding.day18_http_exam.repository.PokemonRepository
import com.survivalcoding.day18_http_exam.repository.PokemonRepositoryImpl
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val dataSource: PokemonDataSource = RemotePokemonDataSource()
    val repository: PokemonRepository = PokemonRepositoryImpl(dataSource)

    when (val result = repository.getPokemon("pikachu")) {
        is Result.Error -> {
            when (result.error) {
                NetworkError.NO_INTERNET_CONNECTION -> println("No internet connection")
                NetworkError.SERVER_ERROR -> println("Server error")
                NetworkError.CLIENT_ERROR -> println("Client error")
                NetworkError.TIMEOUT_ERROR -> println("Timeout")
                NetworkError.UNKNOWN -> println("Unknown error")
            }
        }

        is Result.Success -> {
            println(result.data.name)
            println(result.data.imageUrl)
        }
    }

}