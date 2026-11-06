package com.survivalcoding.`18day`.model

import java.io.File

/**
 * 포켓몬 도메인 모델.
 * 게임에서 사용하는 포켓몬의 모든 데이터를 보유합니다.
 * 배틀 로직은 PokemonBattleService에 위임되어 이 클래스는 순수 데이터만 담당합니다 (SRP).
 */
data class Pokemon(
    val id: Int,
    val name: String,
    val type: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int = 0,
    val specialAttack: Int = 0,
    val specialDefense: Int = 0,
    val imageUrl: String? = null,
    val soundUrl: String? = null,
    val abilities: String = ""
) {
    fun summary(): String = "#$id ${name.replaceFirstChar { it.uppercase() }} [$type] 키: $height, 몸무게: $weight, HP: $hp, 특성: $abilities"
    fun isAlive(): Boolean = hp > 0
    fun hasImage(): Boolean = imageUrl?.isNotBlank() == true

    /**
     * 사운드 URL이 설정되어 있고 실제 파일이 존재하는지 확인합니다.
     */
    fun hasSoundFile(): Boolean {
        if (soundUrl?.isNotBlank() != true) return false
        return File(soundUrl).exists()
    }

    /**
     * 사용 가능한 사운드 경로를 반환합니다.
     * 실제 파일이 없으면 null을 반환하여 기본 효과음이 사용되도록 합니다.
     */
    fun getAvailableSoundUrl(): String? {
        return if (hasSoundFile()) soundUrl else null
    }
}
