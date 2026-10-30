package com.survivalcoding.day18_http.repository

import com.survivalcoding.day18_http.model.Post

interface PostRepository {
    suspend fun getPosts(): List<Post>
}