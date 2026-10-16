package com.survivalcoding.day09_data_type

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    var name: String,
    var age: Int,
)