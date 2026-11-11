package com.survivalcoding.day18_design_principles.model

data class Rating(
    val recipeId: Int,
    val userId: Int,
    val score: Int,
)
