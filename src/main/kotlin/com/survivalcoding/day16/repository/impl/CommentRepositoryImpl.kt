package com.survivalcoding.day16.repository.impl

import com.survivalcoding.day16.repository.inter.CommentRepository
import com.survivalcoding.day16.model.Comment
import com.survivalcoding.day16.datasource.inter.CommentDataSource

class CommentRepositoryImpl(
    private val dataSource: CommentDataSource
): CommentRepository {
    override fun getComments(postId: Long): List<Comment> {
        return dataSource
            .loadComments()
            .filter { it.postId == postId }
    }
}