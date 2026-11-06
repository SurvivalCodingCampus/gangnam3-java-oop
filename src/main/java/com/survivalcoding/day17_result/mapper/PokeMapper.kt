package com.survivalcoding.day17_result.mapper

import com.survivalcoding.day17_result.dto.PokeDto
import com.survivalcoding.day17_result.model.Poke

fun PokeDto.toModel(): Poke {
    return Poke(
        id = id ?: 0,
        name = name.orEmpty().ifBlank { "Unknown" },
        baseExperience = baseExperience ?: 0,
        height = height?.takeIf { it >= 0 } ?: 0,
        isDefault = isDefault ?: false,
        order = order ?: 0,
        weight = weight?.takeIf { it >= 0 } ?: 0
    )
}

fun Poke.toDto(): PokeDto {
    return PokeDto(
        id = id,
        name = name,
        baseExperience = baseExperience,
        height = height,
        isDefault = isDefault,
        order = order,
        weight = weight
    )
}