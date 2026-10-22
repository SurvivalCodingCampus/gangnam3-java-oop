package com.survivalcoding.day13_model_repository.repository

import com.survivalcoding.day13_model_repository.datasource.CommentDataSource
import com.survivalcoding.day13_model_repository.datasource.FakeCommentDataSource
import com.survivalcoding.day13_model_repository.model.Comment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CommentRepositoryImplTest {

    private lateinit var dataSource: CommentDataSource
    private lateinit var commentRepository: CommentRepository

    @BeforeEach
    fun setUp() {
        dataSource = FakeCommentDataSource()
        dataSource.saveComments(
            listOf(
                Comment(
                    postId = 1,
                    id = 1,
                    name = "id labore ex et quam laborum",
                    email = "Eliseo@gardner.biz",
                    body = "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium",
                ),
                Comment(
                    postId = 1,
                    id = 2,
                    name = "quo vero reiciendis velit similique earum",
                    email = "Jayne_Kuhic@sydney.com",
                    body = "est natus enim nihil est dolore omnis voluptatem numquam\net omnis occaecati quod ullam at\nvoluptatem error expedita pariatur\nnihil sint nostrum voluptatem reiciendis et",
                ),
                Comment(
                    postId = 1,
                    id = 3,
                    name = "odio adipisci rerum aut animi",
                    email = "Nikita@garfield.biz",
                    body = "quia molestiae reprehenderit quasi aspernatur\naut expedita occaecati aliquam eveniet laudantium\nomnis quibusdam delectus saepe quia accusamus maiores nam est\ncum et ducimus et vero voluptates excepturi deleniti ratione",
                ),
            )
        )

        commentRepository = CommentRepositoryImpl(dataSource)
    }

    @Test
    fun getComments() {
        val comments = commentRepository.getComments(postId = 1)

        assertEquals(3, comments.size)
        assertEquals(listOf(1, 2, 3), comments.map { it.id })
    }

    @Test
    fun getComments_returnsEmpty_whenPostIdNotExists() {
        val comments = commentRepository.getComments(postId = 2)

        assertTrue(comments.isEmpty())
    }
}