package com.survivalcoding.day15_network.model

import com.survivalcoding.day15_network.dto.TodoDto

data class Todo(
    val id: Long,
    val userId: Long,
    val title: String,
    val completed: Boolean,
)

fun Todo.toDto() = TodoDto(
    id = id,
    userId = userId,
    title = title,
    completed = completed
)

fun TodoDto.toModel() = Todo(
    id = id ?: 0,
    userId = userId ?: 0,
    title = title ?: "",
    completed = completed ?: false
)