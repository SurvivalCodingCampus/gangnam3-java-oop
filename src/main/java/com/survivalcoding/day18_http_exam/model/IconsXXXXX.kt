package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IconsXXXXX(
    @SerialName("animated")
    val animated: AnimatedXXXXXX,
    @SerialName("front_default")
    val frontDefault: String
)