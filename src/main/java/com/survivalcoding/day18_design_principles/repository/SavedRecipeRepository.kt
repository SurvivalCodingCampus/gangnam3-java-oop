package com.survivalcoding.day18_design_principles.repository

import com.survivalcoding.day18_design_principles.core.NetworkError
import com.survivalcoding.day18_design_principles.core.Result
import com.survivalcoding.day18_design_principles.model.Recipe

interface SavedRecipeRepository {

    suspend fun getSavedRecipes(userId: Int): Result<List<Recipe>, NetworkError>

    suspend fun saveRecipe(userId: Int, recipeId: Int): Result<Unit, NetworkError>

    suspend fun unsaveRecipe(userId: Int, recipeId: Int): Result<Unit, NetworkError>
}
