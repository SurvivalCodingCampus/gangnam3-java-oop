package com.survivalcoding.`18day`.mapper

// PokemonMapper.kt
fun PokemonDto.toModel(): Pokemon {
    // name이 비어 있거나 null인 경우 기본값 처리
    val validName = name.takeIf { it.isNotBlank() } ?: "알 수 없는 포켓몬"

    return Pokemon(
        id = id ?: 0,                   // id가 null이면 기본값 0
        name = validName,               // 검증된 이름 사용
        height = height ?: 0,           // height가 null이면 0
        weight = weight ?: 0            // weight가 null이면 0
    )
}