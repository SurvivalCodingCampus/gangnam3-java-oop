package com.survivalcoding.`18day`.dto
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PokemonDto(
    val id: Int? = null,
    val name: String? = null,
    val height: Int? = null,
    val weight: Int? = null
) {
    companion object {
        fun fromJson(json: String): PokemonDto {
            return Json.decodeFromString(serializer(), json)
        }
    }
}
