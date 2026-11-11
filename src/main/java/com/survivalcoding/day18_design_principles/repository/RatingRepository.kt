package com.survivalcoding.day18_design_principles.repository

import com.survivalcoding.day18_design_principles.core.NetworkError
import com.survivalcoding.day18_design_principles.core.Result
import com.survivalcoding.day18_design_principles.model.Rating

interface RatingRepository {

    suspend fun getRating(recipeId: Int, userId: Int): Result<Rating?, NetworkError>

    suspend fun saveRating(rating: Rating): Result<Unit, NetworkError>
}
