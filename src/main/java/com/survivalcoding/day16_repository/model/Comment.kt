package com.survivalcoding.day16_repository.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val postId: Int,
    val id: String,
    val name: String,
    val email: String,
    val body: String,
)