package com.survivalcoding.day18.model

import kotlinx.serialization.Serializable

@Serializable
data class Posts(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
)