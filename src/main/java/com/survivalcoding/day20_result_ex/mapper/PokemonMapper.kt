package com.survivalcoding.day20_result_ex.mapper

import com.survivalcoding.day20_result_ex.dto.PokemonDto
import com.survivalcoding.day20_result_ex.model.Pokemon

fun PokemonDto.toModel(): Pokemon {
    return Pokemon(
        id = id ?: 0,
        name = name ?: "",
        imageUrl = sprites?.frontShiny ?: ""
    )
}