package com.survivalcoding.day18_design_principles.model

data class User(
    val id: Int,
    val name: String,
    val profileImageUrl: String,
    val job: String,
    val introduction: String,
    val location: String,
    val followerCount: Int,
    val followingCount: Int,
)
