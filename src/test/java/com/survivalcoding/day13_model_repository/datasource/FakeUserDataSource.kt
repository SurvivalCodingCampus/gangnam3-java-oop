package com.survivalcoding.day13_model_repository.datasource

import com.survivalcoding.day13_model_repository.model.User

class FakeUserDataSource : UserDataSource {

    private var users: List<User> = emptyList()

    override fun loadUsers(): List<User> {
        return users
    }

    override fun saveUsers(users: List<User>) {
        this.users = users
    }
}