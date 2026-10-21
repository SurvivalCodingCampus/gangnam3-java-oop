package com.survivalcoding.day15.datasource

import com.survivalcoding.day15.model.Todo

interface TodoDataSource {
    fun loadTodos(): List<Todo>
    fun saveTodos(todos: List<Todo>)
}