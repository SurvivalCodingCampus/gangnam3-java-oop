package com.survivalcoding.day16.datasource.impl

import com.survivalcoding.day16.datasource.inter.CommentDataSource
import com.survivalcoding.day16.model.Comment
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class FileCommentDataSource(
    private val path: Path,
) : CommentDataSource {

    override fun loadComments(): List<Comment> {
        if (!path.exists()) {
            return emptyList()
        }

        val json = path.readText()
        if (json.isBlank()) {
            return emptyList()
        }

        return Json.decodeFromString<List<Comment>>(json)
    }

    override fun saveComments(comments: List<Comment>) {
        val json = Json.encodeToString(comments)
        path.writeText(json)
    }
}
