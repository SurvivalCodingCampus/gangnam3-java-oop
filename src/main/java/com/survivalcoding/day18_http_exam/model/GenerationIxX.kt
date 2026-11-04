package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationIxX(
    @SerialName("champions")
    val champions: Champions,
    @SerialName("legends-za")
    val legendsZa: LegendsZa,
    @SerialName("scarlet-violet")
    val scarletViolet: ScarletViolet
)