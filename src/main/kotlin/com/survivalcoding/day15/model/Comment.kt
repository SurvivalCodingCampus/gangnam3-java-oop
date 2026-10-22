package com.survivalcoding.day15.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val postId: Long,
    val id: Long,
    val name: String,
    val email: String,
    val body: String,
)
