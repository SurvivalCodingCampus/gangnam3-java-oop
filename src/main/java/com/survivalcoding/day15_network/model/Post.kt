package com.survivalcoding.day15_network.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val userId: Int,
    val id: Int? = null,
    val title: String,
    val body: String,
)
