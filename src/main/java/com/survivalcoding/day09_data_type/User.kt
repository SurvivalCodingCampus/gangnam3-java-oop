package com.survivalcoding.day09_data_type

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val email: String,
) {
    fun toJson() = """
        {
            "name": "$name",
            "email": "$email"
        }
    """.trimIndent()
}