package com.survivalcoding.day15.exercise.data_source

import com.survivalcoding.day15.exercise.model.Todo
import kotlinx.serialization.json.Json
import java.io.File

class FileTodoDataSource : TodoDataSource {

    private val file = File("todo.json")

    override fun loadTodos(): List<Todo> {
        if (!file.exists()) {
            return emptyList()
        }

        val jsonText = file.readText()
        if (jsonText.isBlank()) {
            return emptyList()
        }

        return Json.decodeFromString<List<Todo>>(jsonText)
    }

    override fun saveTodos(todos: List<Todo>) {
        val jsonText = Json.encodeToString(todos)
        file.writeText(jsonText)
    }
}

// 루트 폴더가 아닌 resource 폴더에 있는 파일을 가져오고 싶을 경우 사용하는 코드
//val file = ClassLoader.getSystemResourceAsStream("chart_data.json")
//    ?.bufferedReader()
//    ?.use { it.readText() }
//    ?: error("파일을 찾지 못했습니다")