package com.survivalcoding.day15_network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateTodoRequest(
    val userId: Long,
    val title: String,
    val completed: Boolean = false,
)
