package com.survivalcoding.day16_dto_mapper.data_source

import com.survivalcoding.day16_dto_mapper.dto.TodoDto

class TodoDataSourceImpl : TodoDataSource {

    override suspend fun getTodos(): List<TodoDto> {
        return listOf(
            TodoDto(1, "Todo 1", false),
            TodoDto(2, "Todo 2", true),
            TodoDto(3, "Todo 3", false),
        )
    }
}