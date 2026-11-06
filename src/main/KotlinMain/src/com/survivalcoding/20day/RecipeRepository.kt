package com.example.recipe.domain.repository

import com.example.recipe.domain.model.Recipe
import com.example.recipe.core.Result
import com.example.recipe.core.NetworkError

interface RecipeRepository {
    /**
     * 레시피 목록 전체 조회 (또는 카테고리 필터링)
     */
    suspend fun getRecipes(category: String? = null): Result<List<Recipe>, NetworkError>

    /**
     * 레시피 ID 기반 단건 상세 조회
     */
    suspend fun getRecipeById(id: String): Result<Recipe, NetworkError>

    /**
     * 키워드 및 필터 조건(카테고리, 시간, 평점 등) 기반 레시피 검색
     */
    suspend fun searchRecipes(
        query: String,
        category: String? = null,
        timeFilter: String? = null,
        minRating: Double? = null
    ): Result<List<Recipe>, NetworkError>

    /**
     * 메인 화면용 추천/신규 레시피 목록 조회
     */
    suspend fun getNewRecipes(): Result<List<Recipe>, NetworkError>
}