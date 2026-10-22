package com.survivalcoding.day16
package com.survivalcoding.comment.repository

import com.survivalcoding.comment.model.Comment
import com.survivalcoding.comment.model.CommentDataSource

// [Repository 인터페이스]: 비즈니스 로직에 필요한 데이터 기능 정의
interface CommentRepository {
    suspend fun getComments(postId: Int): List<Comment>
}

// [Repository 구현체]: 데이터소스를 주입받아 비즈니스 규칙(필터링)을 수행
class CommentRepositoryImpl(
    private val dataSource: CommentDataSource // 어떤 기술인지 숨기고 DataSource 인터페이스만 의존[cite: 4]
) : CommentRepository {

    override suspend fun getComments(postId: Int): List<Comment> {
        // 1. 데이터소스로부터 전체 댓글 목록을 가져옴
        val allComments = dataSource.getComments()

        // 2. 전달받은 postId와 일치하는 댓글만 쏙 골라내는 비즈니스 로직 처리[cite: 4]
        return allComments.filter { it.postId == postId }
    }
}