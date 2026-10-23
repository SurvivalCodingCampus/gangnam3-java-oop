package com.survivalcoding.day13_model_repository.datasource

import com.survivalcoding.day13_model_repository.model.Comment
import kotlinx.serialization.json.Json
import java.io.File

class FileCommentDataSource : CommentDataSource {

    private val file = File("comments.json")

    override fun loadComments(): List<Comment> {
        if (!file.exists()) {
            return emptyList()
        }

        val jsonText = file.readText()
        if (jsonText.isBlank()) {
            return emptyList()
        }

        return Json.decodeFromString<List<Comment>>(jsonText)
    }

    override fun saveComments(comments: List<Comment>) {
        val jsonText = Json.encodeToString(comments)
        file.writeText(jsonText)
    }
}