package com.survivalcoding.day13_model_repository.datasource

import com.survivalcoding.day13_model_repository.model.User


interface UserDataSource {

    fun loadUsers(): List<User>

    fun saveUsers(users: List<User>)
}