package com.survivalcoding.day20

enum class NetworkError {
    NO_INTERNET,  // SocketException
    SEVER_ERROR,  // 5xx
    CLIENT_ERROR, // 4xx
    TIMEOUT,
    UNKNOWN,
}