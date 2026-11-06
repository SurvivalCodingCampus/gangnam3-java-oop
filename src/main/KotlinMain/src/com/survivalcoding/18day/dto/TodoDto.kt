package com.survivalcoding.`18day`.dto

// 할 일 데이터 전송 객체
data class TodoDto(
    val userId: Int? = null,
    val id: Int? = null,
    val title: String? = null,
    val isCompleted: Boolean? = null
)
