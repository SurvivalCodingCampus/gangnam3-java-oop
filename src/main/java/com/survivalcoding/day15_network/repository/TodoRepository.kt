package com.survivalcoding.day15_network.repository

import com.survivalcoding.day15_network.model.Todo

interface TodoRepository {

    suspend fun getTodos(): List<Todo>
    suspend fun getTodo(id: Int): Todo?
    suspend fun addTodo(userId: Long, title: String, completed: Boolean = false): Todo?
    suspend fun updateTodo(todo: Todo): Todo?
    suspend fun updateTitle(id: Int, title: String): Todo?
    suspend fun removeTodo(id: Int): Boolean
}