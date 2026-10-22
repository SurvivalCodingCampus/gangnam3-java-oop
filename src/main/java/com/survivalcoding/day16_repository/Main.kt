package com.survivalcoding.day16_repository

import com.survivalcoding.day16_repository.data_source.FileUserDataSource
import com.survivalcoding.day16_repository.model.User
import com.survivalcoding.day16_repository.repository.UserRepository
import com.survivalcoding.day16_repository.repository.UserRepositoryImpl

fun main() {
    val dataSource = FileUserDataSource()
    dataSource.saveUsers(
        listOf(
            User(
                postId = 1,
                id = "1",
                name = "id labore ex et quam laborum",
                email = "Eliseo@gardner.biz",
                body = "laudantium enim quasi est quidem\nmagnam voluptate ipsam eos"
            ),
            User(
                postId = 1,
                id = "2",
                name = "quo vero reiciendis velit similique earum",
                email = "Jayne_Kuhic@sydney.com",
                body = "est natus enim nihil est dolore omnis voluptatem numquam"
            ),
            User(
                postId = 2,
                id = "6",
                name = "et fugit eligendi deleniti quidem qui sint nihil autem",
                email = "Presley.Mueller@myrl.com",
                body = "doloribus at sed quis culpa deserunt consectetur qui praesentium"
            ),
        )
    )

    val repository: UserRepository = UserRepositoryImpl(dataSource)

    println("=== postId 1 댓글 ===")
    repository.getUsers().forEach { comment ->
        println("- [ID: ${comment.id}] ${comment.name} (${comment.email})")
    }
}