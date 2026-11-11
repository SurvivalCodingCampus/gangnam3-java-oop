package com.survivalcoding.`18day`.mapper

import com.survivalcoding.`18day`.dto.PokeApiPokemonDto
import com.survivalcoding.`18day`.model.Pokemon

/**
 * PokeApiPokemonDto → Pokemon 도메인 모델 변환 매퍼
 *
 * 게임이 어떠한 경우에도 중단되지 않도록, 모든 null/빈 값/음수 값에 대해
 * 안전한 기본값을 적용합니다. 어떠한 예외가 발생하더라도
 * 안전한 기본값을 가진 Pokemon 인스턴스를 반환합니다.
 */
object PokemonMapper {

    /**
     * PokeApiPokemonDto를 Pokemon 도메인 모델로 변환합니다.
     *
     * 처리 규칙:
     * - name이 null이거나 공백만 있으면 "알 수 없는 포켓몬"으로 처리
     * - type이 null이거나 공백만 있으면 "unknown"으로 처리
     * - 음수 값 (id, height, weight, hp, attack, defense, baseExperience)은 0으로 처리
     * - imageUrl이 null이거나 공백만 있으면 null로 처리
     * - 어떠한 예외가 발생하더라도 안전한 기본값 Pokemon 반환 (게임 중단 없음)
     */
    fun toModel(dto: PokeApiPokemonDto): Pokemon {
        return try {
            // name 검사: null이거나 공백만 있으면 기본값 사용
            val validName = dto.name.takeIf { it.isNotBlank() } ?: "알 수 없는 포켓몬"

            // type 검사
            val validType = dto.type.takeIf { it.isNotBlank() } ?: "unknown"

            // 음수 값 방지: 모든 수치 필드는 0 이상 보장
            val safeId = if (dto.id <= 0) 0 else dto.id
            val safeHeight = if (dto.height < 0) 0 else dto.height
            val safeWeight = if (dto.weight < 0) 0 else dto.weight
            val safeBaseExp = if (dto.baseExperience < 0) 0 else dto.baseExperience
            val safeHp = if (dto.hp < 0) 0 else dto.hp
            val safeAttack = if (dto.attack < 0) 0 else dto.attack
            val safeDefense = if (dto.defense < 0) 0 else dto.defense

            // imageUrl 검사
            val validImageUrl = dto.imageUrl?.takeIf { it.isNotBlank() }

            Pokemon(
                id = safeId,
                name = validName,
                type = validType,
                height = safeHeight,
                weight = safeWeight,
                baseExperience = safeBaseExp,
                hp = safeHp,
                attack = safeAttack,
                defense = safeDefense,
                imageUrl = validImageUrl
            )
        } catch (e: Exception) {
            // 어떠한 예외가 발생하더라도 게임 중단 없이 안전한 기본값 반환
            Pokemon(
                id = 0,
                name = "알 수 없는 포켓몬",
                type = "unknown",
                height = 0,
                weight = 0,
                baseExperience = 0,
                hp = 0,
                attack = 0,
                defense = 0,
                imageUrl = null
            )
        }
    }
}
