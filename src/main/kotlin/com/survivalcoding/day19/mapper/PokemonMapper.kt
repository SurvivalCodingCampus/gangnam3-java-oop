package com.survivalcoding.day19.mapper

import com.survivalcoding.day18.model.Pokemon
import com.survivalcoding.day19.dto.PokemonDto


fun PokemonDto.toModel(): Pokemon = Pokemon(
    id = id ?: 0,
    name = if (name.isNullOrBlank()) "" else name,
    baseExperience = baseExperience ?: 0,
    height = height ?: 0,
    isDefault = isDefault ?: false,
    order = order ?: 0,
    weight = weight ?: 0,
)

fun Pokemon.toDto(): PokemonDto = PokemonDto(
    id = id,
    name = name,
    baseExperience = baseExperience,
    height = height,
    isDefault = isDefault,
    order = order,
    weight = weight,
)
