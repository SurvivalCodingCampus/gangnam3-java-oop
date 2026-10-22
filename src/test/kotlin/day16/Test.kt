package day16

import com.survivalcoding.day16.datasource.mock.FakeCommentDataSource
import com.survivalcoding.day16.model.Comment
import com.survivalcoding.day16.repository.impl.CommentRepositoryImpl
import com.survivalcoding.day16.repository.inter.CommentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Test {

    @Nested
    inner class `CommentRepository테스트` {
        lateinit var commentRepository: CommentRepository
        lateinit var mockComments: List<Comment>

        @BeforeEach
        fun setUp() {
            mockComments = listOf(
                Comment(
                    postId = 1L,
                    id = 1L,
                    name = "id labore ex et quam laborum",
                    email = "Eliseo@gardner.biz",
                    body = "laudantium enim quasi est quidem magnam"
                ),
                Comment(
                    postId = 1L,
                    id = 2L,
                    name = "quo vero reiciendis velit similique earum",
                    email = "Jayne_Kuhic@sydney.com",
                    body = "est natus enim nihil est dolore omnis"
                ),
                Comment(
                    postId = 2L,
                    id = 3L,
                    name = "odio adipisci rerum aut animi",
                    email = "Nikita@garfield.biz",
                    body = "quia molestiae reprehenderit quasi aspernatur"
                )
            )

            commentRepository = CommentRepositoryImpl(
                FakeCommentDataSource(mockComments)
            )
        }

        @Test
        fun `getComments를 통해 postId에 해당하는 댓글만 가져온다`() {
            val actualComments = commentRepository.getComments(mockComments[1].postId)

            assertThat(actualComments).containsExactly(mockComments[0], mockComments[1])
        }
    }
}