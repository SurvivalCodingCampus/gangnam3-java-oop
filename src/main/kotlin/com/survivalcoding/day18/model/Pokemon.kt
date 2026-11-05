package com.survivalcoding.day18.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Pokemon(
    val id: Int,
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val isDefault: Boolean,
    val order: Int,
    val weight: Int
)