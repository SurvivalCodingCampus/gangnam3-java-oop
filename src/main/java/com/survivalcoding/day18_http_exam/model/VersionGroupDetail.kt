package com.survivalcoding.day18_http_exam.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VersionGroupDetail(
    @SerialName("level_learned_at")
    val levelLearnedAt: Int,
    @SerialName("move_learn_method")
    val moveLearnMethod: MoveLearnMethod,
    @SerialName("version_group")
    val versionGroup: VersionGroup
)