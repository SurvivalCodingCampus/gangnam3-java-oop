package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.datasource.UserDataSource
import com.survivalcoding.day13_model_repository.model.User

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
) : UserRepository {

    override fun getUsers(): List<User> {
        return userDataSource.loadUsers()
    }

    override fun getUsersTop10ByUserName(): List<User> {
        return userDataSource.loadUsers()
            .sortedBy { it.username }
            .take(10)
    }
}