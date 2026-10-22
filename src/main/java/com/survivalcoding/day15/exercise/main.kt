package com.survivalcoding.day15.exercise

import com.survivalcoding.day15.exercise.data_source.FileTodoDataSource
import com.survivalcoding.day15.exercise.data_source.TodoDataSource
import com.survivalcoding.day15.exercise.model.Todo

fun main() {
    // TODO: 학생 구현체 인스턴스 생성
    val dataSource: TodoDataSource = FileTodoDataSource()

    // 1. 저장할 더미 Todo 데이터 (JSONPlaceholde1r 규격 샘플)
    val sampleTodos = listOf(
        Todo(userId = 1L, id = 1L, title = "파일 입출력 과제 제출하기", completed = false),
        Todo(userId = 1L, id = 2L, title = "JSON 직렬화 개념 복습", completed = true),
        Todo(userId = 1L, id = 3L, title = "DataSource 인터페이스 작성", completed = true)
    )

    // 2.파일에 저장
    println("=== 데이터 저장 시도 ===")
    dataSource.saveTodos(sampleTodos)
    println("todos.json 파일 저장이 완료되었습니다.\n")

    // 3. 파일에서 로드
    println("=== 저장된 데이터 불러오기 ===")
    val loadedTodos = dataSource.loadTodos()

    loadedTodos.forEach { todo ->
        val status = if (todo.completed) "[완료]" else "[미완료]"
        println("$status ID: ${todo.id} | ${todo.title} (작성자: User ${todo.userId})")
    }
}
