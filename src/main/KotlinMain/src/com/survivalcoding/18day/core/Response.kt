package com.survivalcoding.`18day`.core

// HTTP 응답을 표현하는 제너릭 래퍼 클래스
data class Response<T>(
    val isSuccessful: Boolean,
    val body: T? = null,
    val code: Int = 200
)
