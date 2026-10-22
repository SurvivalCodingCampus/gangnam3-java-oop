package com.survivalcoding.day15.model

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val userId: Long,
    val id: Long,
    val title: String,
    val completed: Boolean,
)