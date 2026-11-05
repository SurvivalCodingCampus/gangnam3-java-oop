package com.survivalcoding.day16_dto_mapper.mapper

import com.survivalcoding.day16_dto_mapper.dto.TodoDto
import com.survivalcoding.day16_dto_mapper.model.Todo

fun TodoDto.toModel(): Todo {
    return Todo(
        id = id ?: 0,
        title = title.orEmpty(),
        completed = completed ?: false
    )
}

fun Todo.toDto(): TodoDto {
    return TodoDto(
        id = id,
        title = title,
        completed = completed
    )
}