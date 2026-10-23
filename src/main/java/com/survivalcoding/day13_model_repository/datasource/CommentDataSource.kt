package com.survivalcoding.day13_model_repository.datasource

import com.survivalcoding.day13_model_repository.model.Comment

interface CommentDataSource {

    fun loadComments(): List<Comment>

    fun saveComments(comments: List<Comment>)
}