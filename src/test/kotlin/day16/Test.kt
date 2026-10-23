@file:Suppress("NonAsciiCharacters", "ClassName")

package day16

import com.survivalcoding.day16.datasource.inter.CommentDataSource
import com.survivalcoding.day16.datasource.mock.FakeCommentDataSource
import com.survivalcoding.day16.model.Comment
import com.survivalcoding.day16.repository.impl.CommentRepositoryImpl
import com.survivalcoding.day16.repository.inter.CommentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.collections.plus

class Test {

    @Nested
    inner class `FakeCommentDataSource테스트` {
        private lateinit var mockComments: List<Comment>

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
        }

        @Test
        fun `기본 생성자로 생성 시 빈 리스트를 반환한다`() {
            val dataSource: CommentDataSource = FakeCommentDataSource()

            assertThat(dataSource.loadComments()).isEmpty()
        }

        @Test
        fun `초기 데이터로 초기화 시 초기 데이터를 반환한다`() {
            val dataSource: CommentDataSource = FakeCommentDataSource(mockComments)

            val actualComments = dataSource.loadComments()

            assertThat(actualComments)
                .hasSize(3)
                .containsExactlyElementsOf(mockComments)
        }

        @Test
        fun `saveComments를 호출하면 댓글 목록이 append된다`() {
            val dataSource: CommentDataSource = FakeCommentDataSource(mockComments)

            val newComments = listOf(
                Comment(
                    postId = 3L,
                    id = 4L,
                    name = "alias odio sit",
                    email = "Lew@alena.tv",
                    body = "non et atque occaecati facilis esse amet occaecati aut"
                )
            )

            dataSource.saveComments(newComments)
            
            val actualComments = dataSource.loadComments()

            assertThat(actualComments)
                .hasSize(4)
                .containsExactlyElementsOf(mockComments + newComments)
        }
    }

    @Nested
    inner class `CommentRepository테스트` {
        private lateinit var commentRepository: CommentRepository
        private lateinit var mockComments: List<Comment>

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
        fun `getComments를 통해 postId에 해당하는 댓글을 가져온다`() {
            val actualComments = commentRepository.getComments(1L)

            assertThat(actualComments)
                .hasSize(2)
                .containsExactly(mockComments[0], mockComments[1])
        }

        @Test
        fun `존재하지 않는 postId로 조회 시 빈 리스트를 반환한다`() {
            val actualComments = commentRepository.getComments(0L)

            assertThat(actualComments).isEmpty()
        }
    }
}