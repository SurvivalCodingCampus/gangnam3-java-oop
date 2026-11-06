package com.survivalcoding.`18day`.pattern.adapter

import com.survivalcoding.`18day`.dto.PokeApiPokemonDto
import com.survivalcoding.`18day`.model.Pokemon

/**
 * [GoF Adapter Pattern] PokeAPI DTO → Pokemon 도메인 모델 어댑터.
 *
 * PokeAPI 응답 DTO(PokeApiPokemonDto)와 게임 도메인 모델(Pokemon)의
 * 인터페이스 불일치를 해결합니다. DTO의 중첩 구조(stats, types, sprites, cries, abilities)
 *를 Pokemon의 평탄한 필드로 변환합니다.
 *
 * SRP 준수: DTO→도메인 변환 로직만 담당.
 * DIP 준수: Pokemon 도메인 모델에만 의존하며, DTO 내부 구조 변경에 대응하기 쉬움.
 */
class PokeApiPokemonAdapter {

    /**
     * PokeApiPokemonDto를 Pokemon 도메인 모델로 변환합니다.
     */
    fun adapt(dto: PokeApiPokemonDto): Pokemon {
        return Pokemon(
            id = if (dto.id <= 0) 0 else dto.id,
            name = dto.name.takeIf { it.isNotBlank() } ?: "알 수 없는 포켓몬",
            type = dto.type.takeIf { it.isNotBlank() } ?: "unknown",
            height = dto.height.coerceAtLeast(0),
            weight = dto.weight.coerceAtLeast(0),
            baseExperience = dto.baseExperience.coerceAtLeast(0),
            hp = dto.hp.coerceAtLeast(1),
            attack = dto.attack.coerceAtLeast(0),
            defense = dto.defense.coerceAtLeast(0),
            speed = dto.speed.coerceAtLeast(0),
            specialAttack = dto.specialAttack.coerceAtLeast(0),
            specialDefense = dto.specialDefense.coerceAtLeast(0),
            imageUrl = dto.imageUrl,
            soundUrl = dto.cryUrl,
            abilities = dto.abilityList
        )
    }

    /**
     * 여러 DTO를 한번에 변환합니다.
     */
    fun adaptAll(dtos: List<PokeApiPokemonDto>): List<Pokemon> {
        return dtos.map { adapt(it) }
    }

    companion object {
        @JvmStatic
        fun create(): PokeApiPokemonAdapter = PokeApiPokemonAdapter()
    }
}
