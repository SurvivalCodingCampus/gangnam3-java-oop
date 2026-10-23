package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.datasource.CommentDataSource
import com.survivalcoding.day13_model_repository.model.Comment

class CommentRepositoryImpl(
    private val commentDataSource: CommentDataSource,
) : CommentRepository {

    override fun getComments(postId: Int): List<Comment> {
        return commentDataSource.loadComments()
            .filter { it.postId == postId }
    }
}