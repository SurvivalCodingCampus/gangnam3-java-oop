package com.survivalcoding.day15_network.datasource

import com.survivalcoding.day15_network.dto.CreateTodoRequest
import com.survivalcoding.day15_network.dto.PatchTodoRequest
import com.survivalcoding.day15_network.dto.TodoDto
import com.survivalcoding.day15_network.dto.UpdateTodoRequest
import com.survivalcoding.day15_network.model.Response

interface TodoDataSource {

    suspend fun getTodos(): Response<List<TodoDto>?>
    suspend fun getTodo(id: Int): Response<TodoDto?>
    suspend fun postTodo(request: CreateTodoRequest): Response<TodoDto?>
    suspend fun putTodo(id: Int, request: UpdateTodoRequest): Response<TodoDto?>
    suspend fun patchTodo(id: Int, request: PatchTodoRequest): Response<TodoDto?>
    suspend fun deleteTodo(id: Int) : Response<Unit>
}