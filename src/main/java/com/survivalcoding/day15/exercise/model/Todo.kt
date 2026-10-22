package com.survivalcoding.day15.exercise.model

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val userId: Long,
    val id: Long,
    val title: String,
    val completed: Boolean
)