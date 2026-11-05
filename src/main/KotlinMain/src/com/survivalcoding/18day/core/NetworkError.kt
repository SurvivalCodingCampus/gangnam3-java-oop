package com.survivalcoding.`18day`.core

// 네트워크 오류 유형을 정의한 열거형
enum class NetworkError {
    CLIENT_ERROR,    // 400번대 클라이언트 오류 (예: 404 Not Found)
    SERVER_ERROR,    // 500번대 서버 오류 또는 빈 응답
    NO_INTERNET,     // 인터넷 연결 없음 (SocketException)
    TIMEOUT,         // 요청 시간 초과
    UNKNOWN          // 기타 알 수 없는 오류
}
