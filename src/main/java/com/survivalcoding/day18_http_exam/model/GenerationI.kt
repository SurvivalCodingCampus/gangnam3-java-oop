package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationI(
    @SerialName("red-and-blue")
    val redAndBlue: RedAndBlue,
    @SerialName("red-and-green")
    val redAndGreen: RedAndGreen
)