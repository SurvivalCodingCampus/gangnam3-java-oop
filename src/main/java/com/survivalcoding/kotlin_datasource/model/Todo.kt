package com.survivalcoding.kotlin_datasource.model

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
	val userId: Long,  // id는 보통 Long 사용해서 값을 여유롭게 주는 편
	val id: Long,
	val title: String,
	val completed: Boolean,
)
