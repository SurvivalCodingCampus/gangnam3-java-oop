package com.survivalcoding.generic_enum_string

enum class AuthState {
    AUTHENTICATED,
    UNAUTHENTICATED,
    UNKNOWN,
}

fun main() {
    val authState = AuthState.UNAUTHENTICATED

    loginProcess(authState)
}

fun loginProcess(authState: AuthState) {
    when (authState) {
        AuthState.AUTHENTICATED -> {
            println("인증됨")
            println("잘됨")
        }
        AuthState.UNAUTHENTICATED -> println("인증 안됨")
        AuthState.UNKNOWN -> println("알 수 없음")
    }
}