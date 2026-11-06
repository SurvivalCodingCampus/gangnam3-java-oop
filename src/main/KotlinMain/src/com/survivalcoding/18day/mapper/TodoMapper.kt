package com.survivalcoding.`18day`.mapper

import com.survivalcoding.`18day`.dto.TodoDto
import com.survivalcoding.`18day`.model.Todo

// TodoDto -> Todo 도메인 모델 변환기
fun TodoDto.toModel(): Todo {
    return Todo(
        userId = userId ?: 0,
        id = id ?: 0,
        title = title ?: "제목없음",
        isCompleted = isCompleted ?: false,
    )
}
