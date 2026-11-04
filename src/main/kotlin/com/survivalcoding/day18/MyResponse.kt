package com.survivalcoding.day18

class MyResponse<T> (
    val statusCode: StatusCode,
    val headers: Map<String, List<String>> = emptyMap(),
    val body: T
)

