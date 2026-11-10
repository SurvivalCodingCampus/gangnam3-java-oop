package com.survivalcoding.day18_design_principles.model

data class RecipeFilter(
    val sortType: SortType = SortType.ALL,
    val rating: Int? = null,
    val category: String? = null,
)

enum class SortType {
    ALL,
    NEWEST,
    OLDEST,
    POPULARITY,
}
