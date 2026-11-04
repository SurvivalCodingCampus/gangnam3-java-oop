package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RedAndBlue(
    @SerialName("front_default")
    val frontDefault: String
)