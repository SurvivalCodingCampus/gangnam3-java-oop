package com.example.recipe.domain.repository

import com.example.recipe.domain.model.User
import com.example.recipe.core.Result
import com.example.recipe.core.NetworkError

interface UserRepository {
    /**
     * 현재 로그인한 사용자 프로필 정보 조회
     */
    suspend fun getCurrentUserProfile(): Result<User, NetworkError>

    /**
     * 특정 셰프/사용자의 프로필 정보 조회
     */
    suspend fun getUserProfile(userId: String): Result<User, NetworkError>

    /**
     * 셰프 팔로우 / 언팔로우 상태 변경
     */
    suspend fun toggleFollowUser(targetUserId: String): Result<Boolean, NetworkError>
}