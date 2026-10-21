package com.survivalcoding.day12_datasource.model

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val id: Long,
    val userId: Long,
    val title: String,
    val completed: Boolean
)
