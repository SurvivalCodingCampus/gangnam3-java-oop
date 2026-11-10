package com.survivalcoding.day18_design_principles.model

import java.time.LocalDateTime

data class Recipe(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val videoUrl: String?,
    val chef: User,
    val category: String,
    val rating: Double,
    val reviewCount: Int,
    val cookingTimeMinutes: Int,
    val serving: Int,
    val ingredients: List<Ingredient>,
    val procedures: List<Procedure>,
    val createdAt: LocalDateTime,
)
