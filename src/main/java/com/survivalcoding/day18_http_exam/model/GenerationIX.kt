package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationIX(
    @SerialName("red-blue")
    val redBlue: RedBlue,
    @SerialName("red-green-japan")
    val redGreenJapan: RedGreenJapan,
    @SerialName("yellow")
    val yellow: Yellow
)