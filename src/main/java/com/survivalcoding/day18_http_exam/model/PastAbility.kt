package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PastAbility(
    @SerialName("abilities")
    val abilities: List<AbilityXX>,
    @SerialName("generation")
    val generation: Generation
)