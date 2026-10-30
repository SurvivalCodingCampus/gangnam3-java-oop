package com.survivalcoding.day18_http.datasource

interface PostDataSource {
    suspend fun getPosts(): Response
    suspend fun getPost(id: Int): Response
}