package com.survivalcoding.day18_design_principles.model

import java.time.LocalDateTime

data class Review(
    val id: Int,
    val recipeId: Int,
    val writer: User,
    val content: String,
    val likeCount: Int,
    val dislikeCount: Int,
    val createdAt: LocalDateTime,
)
