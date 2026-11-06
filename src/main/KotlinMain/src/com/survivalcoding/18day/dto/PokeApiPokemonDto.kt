package com.survivalcoding.`18day`.dto

import com.survivalcoding.`18day`.model.Pokemon
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * PokeAPI (https://pokeapi.co/api/v2/pokemon/{name}) 응답 중 게임에 필요한 필드만 선별한 DTO
 *
 * PokeAPI 실제 응답 구조(중첩된 stats, types, sprites, cries, abilities)에 맞게 정의하며,
 * 게임에 필요한 핵심 데이터(타입, HP, 공격력, 방어력, 이미지 URL, 사운드 URL, 특성)를
 * computed property로 노출합니다.
 */
@Serializable
data class PokeApiPokemonDto(
    @SerialName("id")
    val id: Int = 0,

    @SerialName("name")
    val name: String = "",

    @SerialName("height")
    val height: Int = 0,

    @SerialName("weight")
    val weight: Int = 0,

    @SerialName("base_experience")
    val baseExperience: Int = 0,

    @SerialName("stats")
    val stats: List<StatDto> = emptyList(),

    @SerialName("types")
    val types: List<TypeDto> = emptyList(),

    @SerialName("sprites")
    val sprites: SpritesDto? = null,

    @SerialName("cries")
    val cries: CriesDto? = null,

    @SerialName("abilities")
    val abilities: List<AbilityDto> = emptyList(),

    @SerialName("order")
    val order: Int = 0,

    @SerialName("is_default")
    val isDefault: Boolean = false
) {
    /** 포켓몬 타입 (types 배열의 첫 번째 타입 이름, 기본값 "unknown") */
    val type: String get() = types.firstOrNull()?.type?.name ?: "unknown"

    /** 체력 (stats 중 stat.name == "hp" 인 base_stat) */
    val hp: Int get() = stats.firstOrNull { it.stat?.name == "hp" }?.baseStat ?: 0

    /** 공격력 (stats 중 stat.name == "attack" 인 base_stat) */
    val attack: Int get() = stats.firstOrNull { it.stat?.name == "attack" }?.baseStat ?: 0

    /** 방어력 (stats 중 stat.name == "defense" 인 base_stat) */
    val defense: Int get() = stats.firstOrNull { it.stat?.name == "defense" }?.baseStat ?: 0

    /** 스피드 (stats 중 stat.name == "speed" 인 base_stat) */
    val speed: Int get() = stats.firstOrNull { it.stat?.name == "speed" }?.baseStat ?: 0

    /** 특수공격 (stats 중 stat.name == "special-attack" 인 base_stat) */
    val specialAttack: Int get() = stats.firstOrNull { it.stat?.name == "special-attack" }?.baseStat ?: 0

    /** 특수방어 (stats 중 stat.name == "special-defense" 인 base_stat) */
    val specialDefense: Int get() = stats.firstOrNull { it.stat?.name == "special-defense" }?.baseStat ?: 0

    /** 스프라이트 이미지 URL (sprites.front_default) */
    val imageUrl: String? get() = sprites?.frontDefault

    /** 포켓몬 울음소리 URL (cries.latest) */
    val cryUrl: String? get() = cries?.latest

    /** 첫 번째 특성 이름 (기본값 "없음") */
    val firstAbility: String get() = abilities.firstOrNull()?.ability?.name ?: "없음"

    /** 모든 특성 이름 목록 (쉼표 구분) */
    val abilityList: String get() = abilities.map { it.ability?.name ?: "알 수 없음" }.joinToString(", ")

    /**
     * 포켓몬의 간략 정보 문자열을 반환합니다.
     */
    fun summary(): String = "#${id} ${name.replaceFirstChar { it.uppercase() }} [$type] 키: $height, 몸무게: $weight, HP: $hp, 특성: $firstAbility"

    /**
     * PokeApiPokemonDto → Pokemon 도메인 모델로 변환합니다.
     * PokeAPI의 cries.latest URL을 사운드 URL로 사용합니다.
     */
    fun toDomain(): Pokemon {
        return Pokemon(
            id = if (this.id <= 0) 0 else this.id,
            name = this.name.takeIf { it.isNotBlank() } ?: "알 수 없는 포켓몬",
            type = this.type.takeIf { it.isNotBlank() } ?: "unknown",
            height = if (this.height < 0) 0 else this.height,
            weight = if (this.weight < 0) 0 else this.weight,
            baseExperience = if (this.baseExperience < 0) 0 else this.baseExperience,
            hp = if (this.hp < 0) 0 else this.hp,
            attack = if (this.attack < 0) 0 else this.attack,
            defense = if (this.defense < 0) 0 else this.defense,
            speed = if (this.speed < 0) 0 else this.speed,
            specialAttack = if (this.specialAttack < 0) 0 else this.specialAttack,
            specialDefense = if (this.specialDefense < 0) 0 else this.specialDefense,
            imageUrl = this.imageUrl?.takeIf { it.isNotBlank() },
            soundUrl = this.cryUrl?.takeIf { it.isNotBlank() },
            abilities = this.abilityList
        )
    }

    @Serializable
    data class StatDto(
        @SerialName("base_stat")
        val baseStat: Int = 0,
        @SerialName("stat")
        val stat: StatNameDto? = null
    ) {
        @Serializable
        data class StatNameDto(
            @SerialName("name")
            val name: String = ""
        )
    }

    @Serializable
    data class TypeDto(
        @SerialName("type")
        val type: TypeNameDto? = null
    ) {
        @Serializable
        data class TypeNameDto(
            @SerialName("name")
            val name: String = ""
        )
    }

    @Serializable
    data class SpritesDto(
        @SerialName("front_default")
        val frontDefault: String? = null
    )

    @Serializable
    data class CriesDto(
        @SerialName("latest")
        val latest: String? = null,
        @SerialName("legacy")
        val legacy: String? = null
    )

    @Serializable
    data class AbilityDto(
        @SerialName("ability")
        val ability: AbilityNameDto? = null,
        @SerialName("is_hidden")
        val isHidden: Boolean = false,
        @SerialName("slot")
        val slot: Int = 0
    ) {
        @Serializable
        data class AbilityNameDto(
            @SerialName("name")
            val name: String = ""
        )
    }

    companion object {
        private val serializer = kotlinx.serialization.serializer<PokeApiPokemonDto>()

        fun fromJson(json: String): PokeApiPokemonDto {
            return Json.decodeFromString(serializer, json)
        }
    }
}
