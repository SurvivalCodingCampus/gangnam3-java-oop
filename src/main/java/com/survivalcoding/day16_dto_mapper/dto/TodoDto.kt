package com.survivalcoding.day16_dto_mapper.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoDto(
    val id: Int? = null,
    val title: String? = null,
    val completed: Boolean? = null,
)
