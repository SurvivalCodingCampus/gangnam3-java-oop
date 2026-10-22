package com.survivalcoding.kotlin_datasource.data_source

import com.survivalcoding.kotlin_datasource.model.Todo

interface TodoDataSource {
	fun loadTodos(): List<Todo>
	fun saveTodos(todos: List<Todo>)
}