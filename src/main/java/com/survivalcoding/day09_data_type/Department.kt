package com.survivalcoding.day09_data_type

import kotlinx.serialization.Serializable

@Serializable
data class Department(
    var name: String,
    var leader: Employee,
)