package com.survivalcoding.day18_design_principles.repository

import com.survivalcoding.day18_design_principles.core.NetworkError
import com.survivalcoding.day18_design_principles.core.Result
import com.survivalcoding.day18_design_principles.model.Recipe

interface RecentSearchRepository {

    suspend fun getRecentSearches(userId: Int): Result<List<Recipe>, NetworkError>

    suspend fun addRecentSearch(userId: Int, recipeId: Int): Result<Unit, NetworkError>
}
