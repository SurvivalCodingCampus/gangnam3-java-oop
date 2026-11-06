package com.survivalcoding.day20_result_ex.core

data class Response<T> (
    val statusCode Int = 200,
    val headers: Map<String, String> = mapOf("Content-Type" to "application/json"),
    val body: T? = null,
)
}