package com.survivalcoding.day17_result.model

data class Response<T>(
    val statusCode: Int,
    val headers: Map<String, String> = emptyMap(),
    val body: T
) {
    val isSuccess: Boolean get() = statusCode in 200..299
}
