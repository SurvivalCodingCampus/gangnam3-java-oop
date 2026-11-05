package com.survivalcoding.day19.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PokemonDto (
    val id: Int?,
    val name: String?,
    @SerialName("base_experience")
    val baseExperience: Int?,
    val height: Int?,
    @SerialName("is_default")
    val isDefault: Boolean?,
    val order: Int?,
    val weight: Int?
)

