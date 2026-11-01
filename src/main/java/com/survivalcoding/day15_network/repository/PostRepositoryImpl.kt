package com.survivalcoding.day15_network.repository

import com.survivalcoding.day15_network.datasource.PostDataSource
import com.survivalcoding.day15_network.model.Post
import kotlinx.coroutines.CancellationException

class PostRepositoryImpl(
    private val dataSource: PostDataSource
) : PostRepository {

    override suspend fun getPosts(): List<Post> {
        return try {
            val response = dataSource.getPosts()
            if (response.statusCode in 200..299) response.body ?: emptyList() else emptyList()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getPost(id: Int): Post? {
        return try {
            val response = dataSource.getPost(id)
            if (response.statusCode in 200..299) response.body else null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createPost(post: Post): Post? {
        return try {
            val response = dataSource.createPost(post)
            if (response.statusCode in 200..299) response.body else null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updatePost(post: Post): Boolean {
        val id = post.id ?: return false

        return try {
            val response = dataSource.updatePost(id, post)
            response.statusCode in 200..299
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deletePost(id: Int): Boolean {
        return try {
            val response = dataSource.deletePost(id)
            response.statusCode in 200..299
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getPostsByKeyword(keyword: String): List<Post> {
        return try {
            val response = dataSource.getPosts()
            if (response.statusCode in 200..299) {
                response.body?.filter { it.title.contains(keyword) } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emptyList()
        }
    }
}