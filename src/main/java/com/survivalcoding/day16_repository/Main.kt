package com.survivalcoding.day16_repository

import com.survivalcoding.day16_repository.data_source.FileCommentDataSource
import com.survivalcoding.day16_repository.model.Comment
import com.survivalcoding.day16_repository.repository.CommentRepository
import com.survivalcoding.day16_repository.repository.CommentRepositoryImpl

fun main() {
    val dataSource = FileCommentDataSource()
    dataSource.saveComments(
        listOf(
            Comment(
                postId = 1,
                id = "1",
                name = "id labore ex et quam laborum",
                email = "Eliseo@gardner.biz",
                body = "laudantium enim quasi est quidem\nmagnam voluptate ipsam eos"
            ),
            Comment(
                postId = 1,
                id = "2",
                name = "quo vero reiciendis velit similique earum",
                email = "Jayne_Kuhic@sydney.com",
                body = "est natus enim nihil est dolore omnis voluptatem numquam"
            ),
            Comment(
                postId = 2,
                id = "6",
                name = "et fugit eligendi deleniti quidem qui sint nihil autem",
                email = "Presley.Mueller@myrl.com",
                body = "doloribus at sed quis culpa deserunt consectetur qui praesentium"
            ),
        )
    )

    val repository: CommentRepository = CommentRepositoryImpl(dataSource)

    println("=== postId 1 댓글 ===")
    repository.getComments(1).forEach { comment ->
        println("- [ID: ${comment.id}] ${comment.name} (${comment.email})")
    }
}