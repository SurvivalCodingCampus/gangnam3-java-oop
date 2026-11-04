package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationViii(
    @SerialName("brilliant-diamond-shining-pearl")
    val brilliantDiamondShiningPearl: BrilliantDiamondShiningPearl,
    @SerialName("legends-arceus")
    val legendsArceus: LegendsArceus,
    @SerialName("sword-shield")
    val swordShield: SwordShield
)