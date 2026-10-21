package com.survivalcoding.kotlin_datasource.model

import kotlinx.serialization.Serializable

@Serializable
data class Item (  // 도메인 모델 객체
	val id: Long,  // id는 크게 잡기 위해 보통 long을 사용함
	val name: String,
	val price: Int,
)

