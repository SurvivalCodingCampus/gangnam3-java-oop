package com.survivalcoding.day16.repository.impl

import com.survivalcoding.day16.repository.inter.CommentRepository
import com.survivalcoding.day16.datasource.impl.FileCommentDataSource
import com.survivalcoding.day15.model.Comment

class CommentRepositoryImpl(
    private val dataSource: FileCommentDataSource
): CommentRepository {
    override fun getComments(postId: Long): List<Comment> {
        return dataSource
            .loadComments()
            .filter { it.postId == postId }
    }
}