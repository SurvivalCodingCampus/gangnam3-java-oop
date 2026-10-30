package com.survivalcoding.day18_http.repository

import com.survivalcoding.day18_http.datasource.PostDataSource
import com.survivalcoding.day18_http.model.Post
import kotlinx.serialization.json.Json

class PostRepositoryImpl(
    private val dataSource: PostDataSource,
) : PostRepository {

    override suspend fun getPosts(): List<Post> {
        try {
            val response = dataSource.getPosts()

            // 실패
            if (response.statusCode != 200) {
                return emptyList()
            }

            // 성공 null
            if (response.body == null) {
                return emptyList()
            }
            // null 아님
            return Json.decodeFromString<List<Post>>(response.body)
        } catch (ex: Exception) {
            return emptyList()
        }
    }
}