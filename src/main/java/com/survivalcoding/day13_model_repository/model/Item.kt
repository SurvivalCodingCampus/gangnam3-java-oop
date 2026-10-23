package com.survivalcoding.day13_model_repository.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Long,
    val name: String,
    val price: Int
)
