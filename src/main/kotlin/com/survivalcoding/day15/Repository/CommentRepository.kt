package com.survivalcoding.day15.Repository

import com.survivalcoding.day15.model.Comment

interface CommentRepository {
    fun getComments(postId: Long): List<Comment>
}