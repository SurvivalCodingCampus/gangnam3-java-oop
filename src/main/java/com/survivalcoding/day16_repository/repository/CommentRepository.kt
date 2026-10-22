package com.survivalcoding.day16_repository.repository

import com.survivalcoding.day16_repository.model.Comment

interface CommentRepository {
    fun getComments(postId: Int): List<Comment>
}