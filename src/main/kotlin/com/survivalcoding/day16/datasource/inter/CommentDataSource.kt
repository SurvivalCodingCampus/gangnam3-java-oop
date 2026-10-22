package com.survivalcoding.day16.datasource.inter

import com.survivalcoding.day15.model.Comment

interface CommentDataSource {
    fun loadComments(): List<Comment>
    fun saveComments(comments: List<Comment>)
}
