package com.survivalcoding.comment.model

import kotlinx.serialization.Serializable

// [Model 클래스]: 댓글 정보를 예쁘게 담아두는 순수한 데이터 틀
@Serializable
data class Comment(
    val postId: Int,    // 게시글 번호
    val id: Int,        // 댓글 번호
    val name: String,   // 작성자 이름
    val email: String,  // 작성자 이메일
    val body: String    // 댓글 내용
)

// [DataSource 인터페이스]: 외부에서 댓글 데이터를 가져오는 통로 정의
interface CommentDataSource {
    suspend fun getComments(): List<Comment>
}

// [Fake DataSource]: 테스트를 위해 메모리 상에서 가짜 댓글 목록을 돌려주는 구현체
class FakeCommentDataSource(
    private val comments: List<Comment> = emptyList()
) : CommentDataSource {
    override suspend fun getComments(): List<Comment> {
        return comments
    }
}