package com.survivalcoding.day13_model_repository.datasource

import com.survivalcoding.day13_model_repository.model.Comment

class FakeCommentDataSource : CommentDataSource {

    private var comments: List<Comment> = emptyList()

    override fun loadComments(): List<Comment> {
        return comments
    }

    override fun saveComments(comments: List<Comment>) {
        this.comments = comments
    }
}
