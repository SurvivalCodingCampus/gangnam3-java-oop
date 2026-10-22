package com.survivalcoding.day16_repository.data_source

import com.survivalcoding.day16_repository.model.Comment

class FakeCommentDataSource: CommentDataSource {
    private var comments: List<Comment> = emptyList()

    override fun loadComments(): List<Comment> {
        return comments
    }

    override fun saveComments(comments: List<Comment>) {
        this.comments = comments
    }
}