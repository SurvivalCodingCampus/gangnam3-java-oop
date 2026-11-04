package com.survivalcoding.day18_http.repository

import com.survivalcoding.day18_http.datasource.PostDataSource
import com.survivalcoding.day18_http.model.Post
import kotlinx.serialization.json.Json

class PostRepositoryImpl(
    private val dataSource: PostDataSource,
) : PostRepository {

    override suspend fun getPosts(): List<Post> {
        val response = dataSource.getPosts()

        // null 아님
        return Json.decodeFromString<List<Post>>(response.body!!)
    }
}