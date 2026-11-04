package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Versions(
    @SerialName("generation-i")
    val generationI: GenerationI,
    @SerialName("generation-ii")
    val generationIi: GenerationIi
)