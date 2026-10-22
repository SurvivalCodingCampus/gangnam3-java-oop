package com.survivalcoding.day12_datasource.datasource

import com.survivalcoding.day12_datasource.model.Todo

interface TodoDataSource {

    fun loadTodos(): List<Todo>

    fun saveTodos(todos: List<Todo>)
}