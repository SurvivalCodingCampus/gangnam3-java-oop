package com.survivalcoding.day15_network.datasource

import com.survivalcoding.day15_network.model.Post
import com.survivalcoding.day15_network.model.Response

// 테스트에서 상태 코드, 데이터, 예외를 정해 Repository가 받을 응답을 만든다
class FakePostDataSource : PostDataSource {

    var statusCode: Int = 200
    var posts: List<Post> = emptyList()
    var exception: Exception? = null

    // DataSource가 호출된 횟수 (요청을 보내지 않아야 하는 경우를 확인할 때 사용)
    var callCount: Int = 0
        private set

    private val isSuccess: Boolean get() = statusCode in 200..299

    override suspend fun getPosts(): Response<List<Post>?> {
        call()
        return Response(statusCode = statusCode, body = if (isSuccess) posts else null)
    }

    override suspend fun getPost(id: Int): Response<Post?> {
        call()
        return Response(statusCode = statusCode, body = if (isSuccess) posts.find { it.id == id } else null)
    }

    override suspend fun createPost(post: Post): Response<Post?> {
        call()
        return Response(statusCode = statusCode, body = if (isSuccess) post.copy(id = 101) else null)
    }

    override suspend fun updatePost(id: Int, post: Post): Response<Post?> {
        call()
        return Response(statusCode = statusCode, body = if (isSuccess) post else null)
    }

    override suspend fun deletePost(id: Int): Response<Unit> {
        call()
        return Response(statusCode = statusCode, body = Unit)
    }

    private fun call() {
        callCount++
        exception?.let { throw it }
    }
}
