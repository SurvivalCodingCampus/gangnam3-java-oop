package com.survivalcoding.day16_dto_mapper.data_source

import com.survivalcoding.day16_dto_mapper.dto.TodoDto

interface TodoDataSource {

    suspend fun getTodos(): List<TodoDto>
}