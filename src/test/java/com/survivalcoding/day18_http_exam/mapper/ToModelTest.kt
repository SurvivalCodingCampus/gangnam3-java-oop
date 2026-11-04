package com.survivalcoding.day18_http_exam.mapper

import com.survivalcoding.day18_http_exam.dto.PokemonDto
import com.survivalcoding.day18_http_exam.model.Pokemon
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ToModelTest {
    @Test
    fun `Dto 가 모델로 잘 변환되어야 한다`() {
        val dto = PokemonDto(
            id = 1,
            name = "name",
        )

        val model: Pokemon = dto.toModel()

        assertEquals(dto.id, model.id)
        assertEquals(dto.name, model.name)
    }

    @Test
    fun `Dto 에 null 이 포함되면 기본값으로 설정되어야 한다`() {
        val dto = PokemonDto(
            id = null,
            name = null,
        )

        val model: Pokemon = dto.toModel()

        assertEquals(0, model.id)
        assertEquals("", model.name)
    }
}