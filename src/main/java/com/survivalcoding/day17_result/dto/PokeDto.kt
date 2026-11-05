package com.survivalcoding.day17_result.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokeDto(
    val id: Int? = null,
    val name: String? = null,
    @SerialName("base_experience")
    val baseExperience: Int? = null,
    val height: Int? = null,
    @SerialName("is_default")
    val isDefault: Boolean? = null,
    val order: Int? = null,
    val weight: Int? = null
)
