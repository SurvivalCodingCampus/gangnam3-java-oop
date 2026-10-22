package com.survivalcoding.day16_repository.data_source

import com.survivalcoding.day16_repository.model.Comment
import java.io.File

class FileCommentDataSource(
    private val filePath: String = "comments.txt"
) : CommentDataSource {

    private val separator = "\t"

    override fun loadComments(): List<Comment> {
        val file = File(filePath)
        if (!file.exists()) return emptyList()

        return file.readLines()
            .filter { it.isNotBlank() }
            .map { line ->
                val parts = line.split(separator)
                Comment(
                    postId = parts[0].toInt(),
                    id = parts[1],
                    name = parts[2],
                    email = parts[3],
                    body = parts[4].replace("\\n", "\n"),
                )
            }
    }

    override fun saveComments(comments: List<Comment>) {
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