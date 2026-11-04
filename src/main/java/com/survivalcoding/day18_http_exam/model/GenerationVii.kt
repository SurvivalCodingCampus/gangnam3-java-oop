package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationVii(
    @SerialName("icons")
    val icons: IconsXXXX,
    @SerialName("lets-go-pikachu-lets-go-eevee")
    val letsGoPikachuLetsGoEevee: LetsGoPikachuLetsGoEevee,
    @SerialName("ultra-sun-ultra-moon")
    val ultraSunUltraMoon: UltraSunUltraMoon
)