package com.survivalcoding.day16.datasource.mock

import com.survivalcoding.day15.model.Comment
import com.survivalcoding.day16.datasource.inter.CommentDataSource
import java.util.Collections.emptyList

class 성(
    initialComments: List<Comment> = emptyList(),
): CommentDataSource {
    private val comments = initialComments.toMutableList()

    override fun loadComments(): List<Comment> {
        return comments.toList()
    }

    override fun saveComments(comments: List<Comment>) {
        this.comments.addAll(comments)
    }
}