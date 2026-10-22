package com.survivalcoding.day16_repository.repository

import com.survivalcoding.day16_repository.model.User

interface UserRepository {
    fun getUsers(): List<User>
    fun getUsersTop10ByUserName(): List<User>
}