package com.survivalcoding.day16_repository.data_source

import com.survivalcoding.day16_repository.model.User
import java.io.File

class FileUserDataSource(
    private val filePath: String = "users.txt"
) : UserDataSource {

    private val separator = "\t"

    override fun loadUsers(): List<User> {
        val file = File(filePath)
        if (!file.exists()) return emptyList()

        return file.readLines()
            .filter { it.isNotBlank() }
            .map { line ->
                val parts = line.split(separator)
                User(
                    postId = parts[0].toInt(),
                    id = parts[1],
                    name = parts[2],
                    email = parts[3],
                    body = parts[4].replace("\\n", "\n"),
                )
            }
    }

    override fun saveUsers(comments: List<User>) {
        val lines = comments.map { comment ->
            listOf(
                comment.postId.toString(),
                comment.id,
                comment.name,
                comment.email,
                comment.body.replace("\n", "\\n"),
            ).joinToString(separator)
        }
        File(filePath).writeText(lines.joinToString("\n"))
    }
}