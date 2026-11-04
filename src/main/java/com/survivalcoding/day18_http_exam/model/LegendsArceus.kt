package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LegendsArceus(
    @SerialName("icons")
    val icons: IconsXXX
)