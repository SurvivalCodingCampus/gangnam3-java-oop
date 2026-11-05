package com.survivalcoding.`18day`.repostory

import com.survivalcoding.`18day`.core.NetworkError
import com.survivalcoding.`18day`.core.Result
import com.survivalcoding.`18day`.model.Todo

// 할 일 레포지토리 인터페이스
interface TodoRepository {
    suspend fun getTodo(id: Int): Result<Todo, NetworkError>
    suspend fun getAllTodos(): Result<List<Todo>, NetworkError>
}
