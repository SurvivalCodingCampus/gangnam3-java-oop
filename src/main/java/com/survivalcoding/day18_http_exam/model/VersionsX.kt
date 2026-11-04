package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionsX(
    @SerialName("generation-i")
    val generationI: GenerationIX,
    @SerialName("generation-ii")
    val generationIi: GenerationIiX,
    @SerialName("generation-iii")
    val generationIii: GenerationIii,
    @SerialName("generation-iv")
    val generationIv: GenerationIv,
    @SerialName("generation-ix")
    val generationIx: GenerationIxX,
    @SerialName("generation-v")
    val generationV: GenerationV,
    @SerialName("generation-vi")
    val generationVi: GenerationVi,
    @SerialName("generation-vii")
    val generationVii: GenerationVii,
    @SerialName("generation-viii")
    val generationViii: GenerationViii
)