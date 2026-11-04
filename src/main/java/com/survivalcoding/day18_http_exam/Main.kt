package com.survivalcoding.day18_http_exam

import com.survivalcoding.day18_http_exam.data_source.PokemonDataSource
import com.survivalcoding.day18_http_exam.data_source.RemotePokemonDataSource
import com.survivalcoding.day18_http_exam.model.Pokemon
import com.survivalcoding.day18_http_exam.repository.PokemonRepository
import com.survivalcoding.day18_http_exam.repository.PokemonRepositoryImpl
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val dataSource: PokemonDataSource = RemotePokemonDataSource()
    val repository: PokemonRepository = PokemonRepositoryImpl(dataSource)

    val pokemon: Pokemon? = repository.getPokemon("pikachu")
    println( pokemon?.sprites?.frontDefault ?: "이미지 없음")
}