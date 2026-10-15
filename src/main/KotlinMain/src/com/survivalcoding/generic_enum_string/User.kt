package com.survivalcoding.generic_enum_string

import kotlinx.serialization.Serializable
import java.io.Serial

@JvmSerializableLamble
data class User(
    val name: String,
    val email: Int

)
class User {
}