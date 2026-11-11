package com.example.recipe.domain.repository

import com.example.recipe.core.Result
import com.example.recipe.core.NetworkError

interface BookmarkRepository {
    /**
     * 저장된(북마크한) 레시피 ID 목록 조회
     */
    suspend fun getSavedRecipeIds(): Result<Set<String>, NetworkError>

    /**
     * 특정 레시피 북마크 상태 토글 (추가 또는 제거)
     * @return 변경된 최종 북마크 여부 (true: 저장됨, false: 해제됨)
     */
    suspend fun toggleBookmark(recipeId: String): Result<Boolean, NetworkError>

    /**
     * 특정 레시피의 북마크 여부 확인
     */
    suspend fun isBookmarked(recipeId: String): Boolean
}