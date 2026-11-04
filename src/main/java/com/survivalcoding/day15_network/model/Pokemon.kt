package com.survivalcoding.day15_network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val id: Int,
    val name: String,
    @SerialName("base_experience")
    val baseExperience: Int,
    val height: Int,
    @SerialName("is_default")
    val isDefault: Boolean,
    val order: Int,
    val weight: Int
)