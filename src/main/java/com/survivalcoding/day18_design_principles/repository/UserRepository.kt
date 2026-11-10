package com.survivalcoding.day18_design_principles.repository

import com.survivalcoding.day18_design_principles.core.NetworkError
import com.survivalcoding.day18_design_principles.core.Result
import com.survivalcoding.day18_design_principles.model.User

interface UserRepository {

    suspend fun getUser(userId: Int): Result<User?, NetworkError>

    suspend fun isFollowing(followerId: Int, followeeId: Int): Result<Boolean, NetworkError>

    suspend fun follow(followerId: Int, followeeId: Int): Result<Unit, NetworkError>

    suspend fun unfollow(followerId: Int, followeeId: Int): Result<Unit, NetworkError>
}
