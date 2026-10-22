package com.survivalcoding.day12kotlin

import com.survivalcoding.comment.model.Comment
import com.survivalcoding.comment.model.FakeCommentDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test



class CommentRepositoryTest {

    @Test
    fun `특정 postId에 해당하는 댓글만 정확히 필터링되는지 테스트`() = runBlocking {
        // 1. 테스트를 위한 가짜 댓글 데이터(Fake Data) 준비
        val fakeComments = listOf(
            Comment(postId = 1, id = 1, name = "철수", email = "chulsoo@test.com", body = "안녕!"),
            Comment(postId = 1, id = 2, name = "영희", email = "younghee@test.com", body = "반가워!"),
            Comment(postId = 2, id = 3, name = "민수", email = "minsu@test.com", body = "다른 글이야")
        )

        // 2. 가짜 데이터소스와 레포지토리 연결
        val dataSource = FakeCommentDataSource(fakeComments)
        val repository = CommentRepositoryImpl(dataSource)

        // 3. postId가 1인 댓글들만 조회 요청
        val result = repository.getComments(postId = 1)

        // 4. 결과 검증: postId가 1인 댓글은 총 2개여야 함[cite: 4]
        assertEquals(2, result.size)
        assertEquals(1, result[0].postId)
        assertEquals(1, result[1].postId)
    }
}