package com.survivalcoding.day15_network.dto

import kotlinx.serialization.Serializable

@Serializable
data class PatchTodoRequest(val title: String)
