package com.survivalcoding.day15_network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoDto(
    val id: Long? = null,
    val userId: Long? = null,
    val title: String? = null,
    val completed: Boolean? = null,
)
