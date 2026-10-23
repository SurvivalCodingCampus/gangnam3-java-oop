package com.survivalcoding.day16_repository.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String,
)