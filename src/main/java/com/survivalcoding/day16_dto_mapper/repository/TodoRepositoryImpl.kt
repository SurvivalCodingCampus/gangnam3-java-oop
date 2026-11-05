package com.survivalcoding.day16_dto_mapper.repository

import com.survivalcoding.day16_dto_mapper.data_source.TodoDataSource
import com.survivalcoding.day16_dto_mapper.mapper.toModel
import com.survivalcoding.day16_dto_mapper.model.Todo

class TodoRepositoryImpl(
    private val dataSource: TodoDataSource
) : TodoRepository {

    override suspend fun getTodos(): List<Todo> {
        val todoDtos = dataSource.getTodos()
        return todoDtos.map { it.toModel() }
    }
}