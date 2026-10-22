package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.model.User


interface UserRepository {

    fun getUsers(): List<User>

    fun getUsersTop10ByUserName(): List<User>
}