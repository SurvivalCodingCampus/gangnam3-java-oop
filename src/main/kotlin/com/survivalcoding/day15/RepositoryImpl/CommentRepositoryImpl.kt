package com.survivalcoding.day15.RepositoryImpl

import com.survivalcoding.day15.Repository.CommentRepository
import com.survivalcoding.day15.datasourceImpl.FileCommentDataSource
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