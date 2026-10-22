package com.survivalcoding.day12_datasource.datasource

import com.survivalcoding.day12_datasource.model.Todo
import kotlinx.serialization.json.Json
import java.io.File

class FileTodoDataSource : TodoDataSource {

    private val file = File("todos.json")

    override fun loadTodos(): List<Todo> {
        if (!file.exists()) {
            return emptyList()
        }

        return Json.decodeFromString<List<Todo>>(file.readText())
            .takeIf { it.isNotEmpty() } ?: emptyList()
    }

    override fun saveTodos(todos: List<Todo>) {
        val jsonText = Json.encodeToString(todos)
        file.writeText(jsonText)
    }
}