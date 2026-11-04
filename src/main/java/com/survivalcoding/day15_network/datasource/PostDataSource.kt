package com.survivalcoding.day15_network.datasource

import com.survivalcoding.day15_network.model.Post
import com.survivalcoding.day15_network.model.Response

interface PostDataSource {

    suspend fun getPosts(): Response<List<Post>?>
    suspend fun getPost(id: Int): Response<Post?>
    suspend fun createPost(post: Post): Response<Post?>
    suspend fun updatePost(id: Int, post: Post): Response<Post?>
    suspend fun deletePost(id: Int): Response<Unit>
}