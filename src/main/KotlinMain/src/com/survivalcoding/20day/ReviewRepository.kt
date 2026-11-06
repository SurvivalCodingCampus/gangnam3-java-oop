package com.example.recipe.domain.repository

import com.example.recipe.domain.model.Review
import com.example.recipe.core.Result
import com.example.recipe.core.NetworkError

interface ReviewRepository {
    /**
     * 특정 레시피의 리뷰 목록 조회
     */
    suspend fun getReviewsByRecipeId(recipeId: String): Result<List<Review>, NetworkError>

    /**
     * 레시피 리뷰 및 평점 등록
     */
    suspend fun addReview(
        recipeId: String,
        rating: Double,
        comment: String
    ): Result<Review, NetworkError>
}