package com.survivalcoding.day16_repository.repository

import com.survivalcoding.day16_repository.data_source.UserDataSource
import com.survivalcoding.day16_repository.model.User

class UserRepositoryImpl(
    private val dataSource: UserDataSource,
) : UserRepository {

    override fun getUsers(): List<User> {
        return dataSource.loadUsers()
    }

    override fun getUsersTop10ByUserName(): List<User> {
        return dataSource.loadUsers()
            .filter { it.postId == postId }
            .take(10)
    }
}