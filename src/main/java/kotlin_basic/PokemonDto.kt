package kotlin_basic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// DTO for: GET https://pokeapi.co/api/v2/pokemon/{pokemon_name}
// Only the commonly used fields are declared. Unknown keys are ignored (see `pokeJson` below).
//Serializable
@Serializable
data class PokemonDto(
    val id: Int? = null,
    val name: String? = null,
    @SerialName("base_experience") val baseExperience: Int? = null,
    val height: Int? = null,            // decimeters (7 -> 0.7 m)
    val weight: Int? = null,            // hectograms (69 -> 6.9 kg)
    val order: Int? = null,
    @SerialName("is_default") val isDefault: Boolean? = null,
    val abilities: List<AbilitySlotDto>? = null,
    val types: List<TypeSlotDto>? = null,
    val stats: List<StatDto>? = null,
    val sprites: SpritesDto? = null,
    val species: NamedApiResourceDto? = null,
    val moves: List<MoveSlotDto>? = null,
    val cries: CriesDto? = null,
)

// Shared shape used all over PokeAPI: { "name": "...", "url": "..." }
@Serializable
data class NamedApiResourceDto(
    val name: String? = null,
    val url: String? = null,
)

@Serializable
data class AbilitySlotDto(
    val ability: NamedApiResourceDto? = null,
    @SerialName("is_hidden") val isHidden: Boolean? = null,
    val slot: Int? = null,
)

@Serializable
data class TypeSlotDto(
    val slot: Int? = null,
    val type: NamedApiResourceDto? = null,
)

@Serializable
data class StatDto(
    @SerialName("base_stat") val baseStat: Int? = null,
    val effort: Int? = null,
    val stat: NamedApiResourceDto? = null,
)

@Serializable
data class MoveSlotDto(
    val move: NamedApiResourceDto? = null,
)

@Serializable
data class SpritesDto(
    @SerialName("front_default") val frontDefault: String? = null,
    @SerialName("back_default") val backDefault: String? = null,
    @SerialName("front_shiny") val frontShiny: String? = null,
    @SerialName("back_shiny") val backShiny: String? = null,
    val other: OtherSpritesDto? = null,
)

@Serializable
data class OtherSpritesDto(
    @SerialName("official-artwork") val officialArtwork: OfficialArtworkDto? = null,
)

@Serializable
data class OfficialArtworkDto(
    @SerialName("front_default") val frontDefault: String? = null,
    @SerialName("front_shiny") val frontShiny: String? = null,
)

@Serializable
data class CriesDto(
    val latest: String? = null,
    val legacy: String? = null,
)

// The real response has many more fields (game_indices, held_items, forms, ...).
// ignoreUnknownKeys lets us skip them instead of crashing.
val pokeJson = Json { ignoreUnknownKeys = true }

// Example:
// val pikachu = pokeJson.decodeFromString<PokemonDto>(responseBody)
// pikachu.name                                   // "pikachu"
// pikachu.types?.map { it.type?.name }           // ["electric"]
// pikachu.stats?.first()?.baseStat               // 35 (hp)
// pikachu.sprites?.other?.officialArtwork?.frontDefault