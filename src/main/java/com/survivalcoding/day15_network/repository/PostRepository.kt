package com.survivalcoding.day15_network.repository

import com.survivalcoding.day15_network.model.Post

interface PostRepository {

    suspend fun getPosts(): List<Post>
    suspend fun getPost(id: Int): Post?
    suspend fun createPost(post: Post): Post?
    suspend fun updatePost(post: Post): Boolean
    suspend fun deletePost(id: Int): Boolean
    suspend fun getPostsByKeyword(keyword: String): List<Post>
}