package com.survivalcoding.day18_design_principles.repository

import com.survivalcoding.day18_design_principles.core.NetworkError
import com.survivalcoding.day18_design_principles.core.Result
import com.survivalcoding.day18_design_principles.model.Recipe
import com.survivalcoding.day18_design_principles.model.RecipeFilter

interface RecipeRepository {

    suspend fun getRecipes(): Result<List<Recipe>, NetworkError>

    suspend fun getRecipe(recipeId: Int): Result<Recipe?, NetworkError>

    suspend fun getRecipesByChef(chefId: Int): Result<List<Recipe>, NetworkError>

    suspend fun searchRecipes(keyword: String, filter: RecipeFilter): Result<List<Recipe>, NetworkError>

    suspend fun getShareLink(recipeId: Int): Result<String, NetworkError>
}
