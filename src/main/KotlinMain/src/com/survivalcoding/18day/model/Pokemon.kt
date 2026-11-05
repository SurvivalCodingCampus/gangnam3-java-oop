package com.survivalcoding.`18day`.model

// 포켓몬 도메인 모델: DTO를 변환하여 사용하는 핵심 엔티티
data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int
)
