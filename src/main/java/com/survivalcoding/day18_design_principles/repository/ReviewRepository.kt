package com.survivalcoding.day18_design_principles.repository

import com.survivalcoding.day18_design_principles.core.NetworkError
import com.survivalcoding.day18_design_principles.core.Result
import com.survivalcoding.day18_design_principles.model.Review

interface ReviewRepository {

    suspend fun getReviews(recipeId: Int): Result<List<Review>, NetworkError>

    suspend fun addReview(recipeId: Int, userId: Int, content: String): Result<Review, NetworkError>

    suspend fun likeReview(reviewId: Int, userId: Int): Result<Unit, NetworkError>

    suspend fun dislikeReview(reviewId: Int, userId: Int): Result<Unit, NetworkError>
}
