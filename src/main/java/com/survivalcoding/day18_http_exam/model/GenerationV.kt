package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationV(
    @SerialName("black-white")
    val blackWhite: BlackWhite,
    @SerialName("icons")
    val icons: IconsXXXXX
)