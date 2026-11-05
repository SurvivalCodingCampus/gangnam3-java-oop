package com.survivalcoding.day16_dto_mapper

import com.survivalcoding.day16_dto_mapper.data_source.PokeDataSource
import com.survivalcoding.day16_dto_mapper.data_source.PokeDataSourceImpl
import com.survivalcoding.day16_dto_mapper.data_source.TodoDataSource
import com.survivalcoding.day16_dto_mapper.data_source.TodoDataSourceImpl
import com.survivalcoding.day16_dto_mapper.repository.PokeRepository
import com.survivalcoding.day16_dto_mapper.repository.PokeRepositoryImpl
import com.survivalcoding.day16_dto_mapper.repository.TodoRepository
import com.survivalcoding.day16_dto_mapper.repository.TodoRepositoryImpl
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val dataSource: TodoDataSource = TodoDataSourceImpl()
    val repository: TodoRepository = TodoRepositoryImpl(dataSource)

    repository.getTodos().forEach(::println)

    val pokeDataSource: PokeDataSource = PokeDataSourceImpl()
    val pokeRepository: PokeRepository = PokeRepositoryImpl(pokeDataSource)

    pokeRepository.getPoke("pikachu")?.let { println(it.name) }
}