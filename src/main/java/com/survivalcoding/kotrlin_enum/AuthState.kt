package com.survivalcoding.kotrlin_enum

enum class AuthState {
	AUTHENTICATED,
	UNAUTHENTICATED,
	UNKNOWN,
}

fun loginProcess(authState: AuthState) {
	when (authState) {  // when에 커서 두고 alt + enter -> add remaining branches 선택하면 모든 케이스 완성됨
		AuthState.AUTHENTICATED -> println("인증됨")
		AuthState.UNAUTHENTICATED -> println("미인증됨")
		AuthState.UNKNOWN -> {
			println("알 수 없음. 중괄호 블럭으로도 생성 가능")
		}
	}
}

fun main() {
	val authState = AuthState.UNAUTHENTICATED
	
	loginProcess(authState)
}