package com.survivalcoding.day15

import com.survivalcoding.day15.datasource.TodoDataSource
import com.survivalcoding.day15.model.Todo
import kotlinx.serialization.json.Json
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

class FileTodoDataSource: TodoDataSource {
    private val path = Path("src/main/resources/todo_data.json")

    override fun loadTodos(): List<Todo> {
        val json = path.readText()

        if (json.isBlank()) {
            return emptyList()
        }

        return Json.decodeFromString<List<Todo>>(json)
    }

    override fun saveTodos(todos: List<Todo>) {
        val json = Json.encodeToString(todos)

        path.writeText(json)
    }

}
