package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.model.Comment

interface CommentRepository {

    fun getComments(postId: Int): List<Comment>
}