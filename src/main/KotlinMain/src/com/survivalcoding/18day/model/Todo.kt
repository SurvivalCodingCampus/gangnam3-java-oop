package com.survivalcoding.`18day`.model

// 할 일 도메인 모델
data class Todo(
    val userId: Int,
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)
