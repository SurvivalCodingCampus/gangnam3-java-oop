package com.survivalcoding.day18_http_exam.mapper

import com.survivalcoding.day18_http_exam.dto.PokemonDto
import com.survivalcoding.day18_http_exam.model.Pokemon

fun PokemonDto.toModel(): Pokemon {
    return Pokemon(
        id = id ?: 0,
        name = name ?: "",
        imageUrl = sprites?.frontDefault ?: ""
    )
}