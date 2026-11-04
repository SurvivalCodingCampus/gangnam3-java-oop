package com.survivalcoding.day16_dto_mapper.model

data class Poke(
    val id: Int,
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val isDefault: Boolean,
    val order: Int,
    val weight: Int
)
