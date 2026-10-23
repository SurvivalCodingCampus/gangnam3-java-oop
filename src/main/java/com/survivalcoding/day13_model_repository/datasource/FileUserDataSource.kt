package com.survivalcoding.day13_model_repository.datasource

import com.survivalcoding.day13_model_repository.model.User
import kotlinx.serialization.json.Json
import java.io.File

class FileUserDataSource(
    private val file: File = File("users.json"),
) : UserDataSource {

    override fun loadUsers(): List<User> {
        if (!file.exists()) {
            return emptyList()
        }

        val jsonText = file.readText()
        if (jsonText.isBlank()) {
            return emptyList()
        }

        return Json.decodeFromString<List<User>>(jsonText)
    }

    override fun saveUsers(users: List<User>) {
        val jsonText = Json.encodeToString(users)
        file.writeText(jsonText)
    }
}