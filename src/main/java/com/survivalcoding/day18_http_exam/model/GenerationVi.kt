package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationVi(
    @SerialName("icons")
    val icons: IconsXXXX,
    @SerialName("omega-ruby-alpha-sapphire")
    val omegaRubyAlphaSapphire: OmegaRubyAlphaSapphire,
    @SerialName("x-y")
    val xY: XY
)