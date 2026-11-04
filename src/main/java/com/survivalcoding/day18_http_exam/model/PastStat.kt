package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PastStat(
    @SerialName("generation")
    val generation: Generation,
    @SerialName("stats")
    val stats: List<StatXX>
)