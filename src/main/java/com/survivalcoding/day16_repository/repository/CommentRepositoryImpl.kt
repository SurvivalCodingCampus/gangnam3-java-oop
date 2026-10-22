package com.survivalcoding.day16_repository.repository

import com.survivalcoding.day16_repository.data_source.CommentDataSource
import com.survivalcoding.day16_repository.model.Comment

class CommentRepositoryImpl(
    private val dataSource: CommentDataSource,
) : CommentRepository {


    override fun getComments(postId: Int): List<Comment> {
        return dataSource.loadComments()
            .filter { it.postId == postId }
    }
}