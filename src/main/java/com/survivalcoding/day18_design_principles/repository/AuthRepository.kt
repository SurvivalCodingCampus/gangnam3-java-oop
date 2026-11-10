package com.survivalcoding.day18_design_principles.repository

import com.survivalcoding.day18_design_principles.core.NetworkError
import com.survivalcoding.day18_design_principles.core.Result
import com.survivalcoding.day18_design_principles.model.User

interface AuthRepository {

    suspend fun signIn(email: String, password: String): Result<User?, NetworkError>

    suspend fun isEmailRegistered(email: String): Result<Boolean, NetworkError>

    suspend fun signUp(name: String, email: String, password: String): Result<User, NetworkError>

    suspend fun signOut(): Result<Unit, NetworkError>

    suspend fun getCurrentUser(): Result<User?, NetworkError>
}
