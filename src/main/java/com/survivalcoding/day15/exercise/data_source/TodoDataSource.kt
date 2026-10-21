package com.survivalcoding.day15.exercise.data_source

import com.survivalcoding.day15.exercise.model.Todo

interface TodoDataSource {
    fun loadTodos(): List<Todo>
    fun saveTodos(todos: List<Todo>)
}