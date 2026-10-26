package com.survivalcoding.day16_repository.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Person(
    @SerialName("Completed")
    val completed: Boolean,
    @SerialName("PersonId")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("userId")
    val userId: Int
)