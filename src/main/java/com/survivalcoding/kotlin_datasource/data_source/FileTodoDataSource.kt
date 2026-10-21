package com.survivalcoding.kotlin_datasource.data_source

import com.survivalcoding.kotlin_datasource.model.Todo
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.system.exitProcess

class FileTodoDataSource: TodoDataSource {
	private val file = File("todos.json")
	
	override fun loadTodos(): List<Todo> {
		if (!file.exists()) {
			return emptyList()
		}
		
		val jsonText = file.readText()  // jsonText[0]은 char '[' 라고 나옴
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