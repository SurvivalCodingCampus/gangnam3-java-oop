package com.survivalcoding.day12_class_generic

// 강의 11번 슬라이드: 정해진 세 가지 인증 상태만 사용
enum class AuthState {
    AUTHENTICATED, UNAUTHENTICATED, UNKNOWN
}

fun main() {
    val authState = AuthState.UNAUTHENTICATED

    loginProcess(authState)
}

fun loginProcess(authState: AuthState) {
    // 모든 enum 항목을 명시해서 새 상태를 추가하면 누락된 처리를 컴파일 시 확인
    when (authState) {
        AuthState.AUTHENTICATED -> {
            println("인증잘됨.")
        }
        AuthState.UNAUTHENTICATED -> {
            println("인증안됨.")
        }
        AuthState.UNKNOWN -> {
            println("알수없음.")
        }
    }
}