package com.survivalcoding.day16.repository.inter

import com.survivalcoding.day15.model.Comment

interface CommentRepository {
    fun getComments(postId: Long): List<Comment>
}