package com.survivalcoding.day15_network.repository

import com.survivalcoding.day15_network.datasource.TodoDataSource
import com.survivalcoding.day15_network.dto.CreateTodoRequest
import com.survivalcoding.day15_network.dto.PatchTodoRequest
import com.survivalcoding.day15_network.dto.UpdateTodoRequest
import com.survivalcoding.day15_network.model.Todo
import com.survivalcoding.day15_network.model.toModel
import kotlin.coroutines.cancellation.CancellationException

class TodoRepositoryImpl(
    private val dataSource: TodoDataSource
) : TodoRepository {

    override suspend fun getTodos(): List<Todo> {
        return try {
            val response = dataSource.getTodos()
            if (response.isSuccess) {
                response.body?.map { it.toModel() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getTodo(id: Int): Todo? {
        return try {
            val response = dataSource.getTodo(id)
            if (response.isSuccess) response.body?.toModel() else null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addTodo(userId: Long, title: String, completed: Boolean): Todo? {
        val request = CreateTodoRequest(
            userId = userId,
            title = title,
            completed = completed,
        )
        return try {
            val response = dataSource.postTodo(request)
            if (response.isSuccess) response.body?.toModel() else null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateTodo(todo: Todo): Todo? {
        val request = UpdateTodoRequest(
            userId = todo.userId,
            title = todo.title,
            completed = todo.completed,
        )

        return try {
            val response = dataSource.putTodo(todo.id.toInt(), request)
            if (response.isSuccess) response.body?.toModel() else null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateTitle(id: Int, title: String): Todo? {
        val request = PatchTodoRequest(title = title)

        return try {
            val response = dataSource.patchTodo(id, request)
            if (response.isSuccess) response.body?.toModel() else null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun removeTodo(id: Int): Boolean {
        return try {
            val response = dataSource.deleteTodo(id)
            response.isSuccess
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            false
        }
    }
}