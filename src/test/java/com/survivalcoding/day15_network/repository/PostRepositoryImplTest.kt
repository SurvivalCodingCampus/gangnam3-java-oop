package com.survivalcoding.day15_network.repository

import com.survivalcoding.day15_network.datasource.FakePostDataSource
import com.survivalcoding.day15_network.model.Post
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class PostRepositoryImplTest {

    private lateinit var dataSource: FakePostDataSource
    private lateinit var postRepository: PostRepository

    @BeforeEach
    fun setUp() {
        dataSource = FakePostDataSource()
        dataSource.posts = listOf(
            postOf(id = 1, title = "Kotlin 코루틴"),
            postOf(id = 2, title = "Ktor 네트워크"),
            postOf(id = 3, title = "Kotlin 테스트"),
        )

        postRepository = PostRepositoryImpl(dataSource)
    }

    @Test
    fun `getPosts는 성공하면 게시글 목록을 반환한다`() = runTest {
        // when
        val posts = postRepository.getPosts()

        // then
        assertEquals(listOf(1, 2, 3), posts.map { it.id })
    }

    @Test
    fun `getPosts는 실패 응답이면 빈 리스트를 반환한다`() = runTest {
        // given
        dataSource.statusCode = 500

        // when
        val posts = postRepository.getPosts()

        // then
        assertTrue(posts.isEmpty())
    }

    @Test
    fun `getPost는 성공하면 게시글을 반환한다`() = runTest {
        // when
        val post = postRepository.getPost(id = 2)

        // then
        assertEquals("Ktor 네트워크", post?.title)
    }

    @Test
    fun `getPost는 실패 응답이면 null을 반환한다`() = runTest {
        // given
        dataSource.statusCode = 404

        // when
        val post = postRepository.getPost(id = 9999)

        // then
        assertNull(post)
    }

    @Test
    fun `createPost는 성공하면 id가 부여된 게시글을 반환한다`() = runTest {
        // when
        val post = postRepository.createPost(postOf(id = null, title = "새 게시글"))

        // then
        assertEquals(101, post?.id)
        assertEquals("새 게시글", post?.title)
    }

    @Test
    fun `createPost는 실패 응답이면 null을 반환한다`() = runTest {
        // given
        dataSource.statusCode = 500

        // when
        val post = postRepository.createPost(postOf(id = null, title = "새 게시글"))

        // then
        assertNull(post)
    }

    @Test
    fun `updatePost는 성공하면 true를 반환한다`() = runTest {
        // when
        val result = postRepository.updatePost(postOf(id = 1, title = "수정된 제목"))

        // then
        assertTrue(result)
    }

    @Test
    fun `updatePost는 실패 응답이면 false를 반환한다`() = runTest {
        // given
        dataSource.statusCode = 404

        // when
        val result = postRepository.updatePost(postOf(id = 9999, title = "수정된 제목"))

        // then
        assertFalse(result)
    }

    @Test
    fun `updatePost는 id가 없으면 요청하지 않고 false를 반환한다`() = runTest {
        // when
        val result = postRepository.updatePost(postOf(id = null, title = "수정된 제목"))

        // then
        assertFalse(result)
        assertEquals(0, dataSource.callCount)
    }

    @Test
    fun `deletePost는 성공하면 true를 반환한다`() = runTest {
        // when
        val result = postRepository.deletePost(id = 1)

        // then
        assertTrue(result)
    }

    @Test
    fun `deletePost는 실패 응답이면 false를 반환한다`() = runTest {
        // given
        dataSource.statusCode = 404

        // when
        val result = postRepository.deletePost(id = 9999)

        // then
        assertFalse(result)
    }

    @Test
    fun `getPostsByKeyword는 제목에 키워드가 포함된 게시글만 반환한다`() = runTest {
        // when
        val posts = postRepository.getPostsByKeyword("Kotlin")

        // then
        assertEquals(listOf(1, 3), posts.map { it.id })
    }

    @Test
    fun `getPostsByKeyword는 일치하는 게시글이 없으면 빈 리스트를 반환한다`() = runTest {
        // when
        val posts = postRepository.getPostsByKeyword("Java")

        // then
        assertTrue(posts.isEmpty())
    }

    @Test
    fun `네트워크 에러가 나면 예외를 던지지 않고 실패 값을 반환한다`() = runTest {
        // given
        dataSource.exception = IOException("서버에 연결할 수 없습니다")

        // when & then
        assertTrue(postRepository.getPosts().isEmpty())
        assertNull(postRepository.getPost(id = 1))
        assertNull(postRepository.createPost(postOf(id = null, title = "새 게시글")))
        assertFalse(postRepository.updatePost(postOf(id = 1, title = "수정된 제목")))
        assertFalse(postRepository.deletePost(id = 1))
    }

    @Test
    fun `코루틴 취소 예외는 처리하지 않고 다시 던진다`() = runTest {
        // given
        dataSource.exception = CancellationException("취소됨")

        // when & then
        assertThrows<CancellationException> {
            postRepository.getPost(id = 1)
        }
    }

    private fun postOf(
        id: Int? = 1,
        title: String = "title",
    ) = Post(
        userId = 1,
        id = id,
        title = title,
        body = "body",
    )
}
