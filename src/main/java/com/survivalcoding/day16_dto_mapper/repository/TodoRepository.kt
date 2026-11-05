package com.survivalcoding.day16_dto_mapper.repository

import com.survivalcoding.day16_dto_mapper.model.Todo

interface TodoRepository {

    suspend fun getTodos(): List<Todo>
}