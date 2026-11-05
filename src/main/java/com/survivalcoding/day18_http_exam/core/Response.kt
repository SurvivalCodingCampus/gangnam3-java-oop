package com.survivalcoding.day18_http_exam.core

data class Response<T>(
    val statusCode: Int = 200,
    val headers: Map<String, String> = mapOf("Content-Type" to "application/json"),
    val body: T? = null,
)
