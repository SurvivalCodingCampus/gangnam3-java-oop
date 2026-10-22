package com.survivalcoding.day16_repository.data_source

import com.survivalcoding.day16_repository.model.User

interface UserDataSource {
    fun loadUsers(): List<User>
    fun saveUsers(users: List<User>)
}
