package com.survivalcoding.day16_repository.data_source

import com.survivalcoding.day16_repository.model.Comment

interface CommentDataSource {
    fun loadComments(): List<Comment>
    fun saveComments(comments: List<Comment>)
}
