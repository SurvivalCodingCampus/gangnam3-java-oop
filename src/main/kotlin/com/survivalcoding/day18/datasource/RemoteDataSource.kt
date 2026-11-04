package com.survivalcoding.day18.datasource

import com.survivalcoding.day18.model.Posts

interface RemoteDataSource {
    suspend fun getPosts(): List<Posts>
    suspend fun getPost(id: Long): Posts
    suspend fun createPost(post: Posts): Posts
    suspend fun updatePost(id: Long, post: Posts): Posts
    suspend fun patchPost(id: Long, post: Posts): Posts
    suspend fun deletePost(id: Long): Boolean
}