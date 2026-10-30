package com.survivalcoding.day18_http.datasource

data class Response(
    val statusCode: Int = 200,
    val headers: Map<String, String> = mapOf("Content-Type" to "application/json"),
    val body: String? = null,
)
