package com.survivalcoding.day20.core

enum class NetworkError {
    NO_INTERNET_ERR,  // SocketException
    SERVER_ERR,  // 5xx
    CLIENT_ERR, // 4xx
    TIMEOUT_ERR,
    UNKNOWN_ERR,
}